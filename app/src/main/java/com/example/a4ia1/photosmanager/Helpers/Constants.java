package com.example.a4ia1.photosmanager.Helpers;

import android.os.Environment;
import java.io.File;

/**
 * Created by vmois on 10/4/17.
 */

public class Constants {
    private static File mainFolderFile;
    private static String mainFolderName = "VladyslavMoisieienkov";
    private static String notesTableName = "notes";

    public Constants() {
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mainFolderFile = new File(pictureFolder, mainFolderName);
    }

    public File getMainFolderFile() {
        return mainFolderFile;
    }

    public String getNotesTableName() {
        return notesTableName;
    }
}
