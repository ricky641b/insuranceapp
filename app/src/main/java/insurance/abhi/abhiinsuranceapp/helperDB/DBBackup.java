package insurance.abhi.abhiinsuranceapp.helperDB;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import insurance.abhi.abhiinsuranceapp.helpers.Constants;

/**
 * Created by rick on 08-07-2017.
 */

public class DBBackup {
    public static String backupName = "Abhi_App_Backup";
    private static File BASE_BACKUP_PATH = Environment.getExternalStoragePublicDirectory
            (DBBackup.backupName);
    private static boolean operationStatus = true;
    private static String appName = "Abhi's Loan App";

    public static void exportDB(Context ctx) {
        try {
            File sd = new File(BASE_BACKUP_PATH.getAbsolutePath());
            File currentDB = ctx.getDatabasePath("loanDB");
            if (!sd.exists()) {
                    operationStatus = sd.mkdirs();
                }
                if (operationStatus) {

                    //String currentDBPath = ctx.getDatabasePath("loanDB").getPath();// "//data//data//insurance.abhi.abhiinsuranceapp//databases//loanDB";
                    String backupDBPath = "db_app_" + Constants.getDateTime();
                    File backupDB = new File(sd, backupDBPath);

                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        //Toast.makeText(getApplicationContext(), "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
                    }
                }
        } catch (Exception e) {
            Log.e("Error",e.getLocalizedMessage());
        }

    }

    public static void importDB(Context ctx,String backupPath) {
        try {
            File sd = BASE_BACKUP_PATH;
            File currentDB = ctx.getDatabasePath("loanDB");

            if (sd.canWrite()) {
                File backupDB = new File(sd, backupPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    //Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error",e.getLocalizedMessage());
        }
    }
    public static List<String> backupFiles()
    {
        List<String> filesName = new ArrayList<>();
        if (BASE_BACKUP_PATH.exists() && BASE_BACKUP_PATH.isDirectory()) {

            File[] files = BASE_BACKUP_PATH.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    filesName.add(file.getName());
                }
            }
        }
        return filesName;
    }
    public static boolean deleteFile(Context ctx,String fileName) {
        try {
            File sd = BASE_BACKUP_PATH;
            if (sd.canWrite()) {
                File backupDB = new File(sd, fileName);
                if (backupDB.delete())
                {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("Error",e.getLocalizedMessage());
        }
        return false;
    }
}