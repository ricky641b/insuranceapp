package insurance.abhi.abhiinsuranceapp.helperDB;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import insurance.abhi.abhiinsuranceapp.helpers.Constants;

/**
 * Created by rick on 08-07-2017.
 */

public class DBBackup {
    public static String backupName = "Abhi_App_Backup";
    public static File BASE_BACKUP_PATH = Environment.getExternalStoragePublicDirectory
            (DBBackup.backupName);
    private static boolean operationStatus = true;
    private static String appName = "Abhi's Loan App";

    public static String exportDB(Context ctx) {
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
                        return "/" + backupName + "/" + backupDBPath;
                        //Toast.makeText(getApplicationContext(), "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
                    }
                }
        } catch (Exception e) {
            Log.e("Error",e.getLocalizedMessage());
        }
        return "n/a";
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
    public static FileInputStream getInputStream(Context ctx) {
        try {
            FileInputStream dst;
            File currentDB = ctx.getDatabasePath("loanDB");
                if (currentDB.exists()) {

                    dst = new FileInputStream(currentDB);
                    return dst;
                    //Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
                }

        } catch (Exception e) {
            Log.e("Error",e.getLocalizedMessage());
        }
        return null;
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
    public static void saveDropboxFileToFolder(InputStream in, String fileName)
    {
        File file = new File(BASE_BACKUP_PATH,fileName);
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}