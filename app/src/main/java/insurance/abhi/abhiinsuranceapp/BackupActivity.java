package insurance.abhi.abhiinsuranceapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudrail.si.CloudRail;
import com.cloudrail.si.services.Dropbox;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import insurance.abhi.abhiinsuranceapp.helperDB.DBBackup;

public class BackupActivity extends AppCompatActivity {

    @BindView(R.id.filePathListView)
    ListView filePathListView;

    List<String> files = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        ButterKnife.bind(this);
        checkPermission();
        restoreFromFolder();
        CloudRail.setAppKey("5960e2b9435ed97ff95c0276");
    }

    @OnClick(R.id.backupfolder)void backupFolder()
    {
        if (checkIfAlreadyhavePermission())
        {
            String path = DBBackup.exportDB(getApplicationContext());
            Toast.makeText(this,"Backup created at "+ path,Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.backupDropbox) void backupToDropbox()
    {
        new DropboxUploadTask().execute();
    }
    @OnClick(R.id.restorefolder) void restoreFromFolder()
    {
        if (checkIfAlreadyhavePermission()) {
            files = DBBackup.backupFiles();
            if (files.size() > 0) {
                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
                filePathListView.setAdapter(itemsAdapter);
                filePathListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String filename = files.get(position);
                        restoreDBConfirmation(filename);
                    }
                });
                filePathListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String filename = files.get(position);
                        deleteEntryConfirmation(filename);
                        return true;
                    }
                });
                Toast.makeText(this, files.size() + " backups found. Tap on filename to restore", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No backups found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void checkPermission()
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && !checkIfAlreadyhavePermission()) {
            requestForSpecificPermission();
        }
    }
    private boolean checkIfAlreadyhavePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Please give permission to this app to access file storage",Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }
    private void requestForSpecificPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }
    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
    void deleteEntryConfirmation(final String fileName)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this backup?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBBackup.deleteFile(getApplicationContext(),fileName);
                        restoreFromFolder();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    void restoreDBConfirmation(final String fileName)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to restore this backup? This will override existing database")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBBackup.importDB(getApplicationContext(), fileName);
                        Toast.makeText(getApplicationContext(), "Backup restored successfully", Toast.LENGTH_SHORT).show();
                        restart();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private class DropboxUploadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled()) {

                Dropbox service = new Dropbox(
                        getApplicationContext(),
                        "tqi92xvjokofrb7",
                        "v4faixtkr99p8kp"
                );
                String path =  DBBackup.exportDB(getApplicationContext());
                FileInputStream backupFileStream = DBBackup.getInputStream(getApplicationContext());
                if(backupFileStream!=null) {
                    service.upload(
                            path,
                            backupFileStream,
                            1024L,
                            true
                    );
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("DONE","DONE");
        }
    }
}
