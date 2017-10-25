package com.example.a4ia1.photosmanager.Helpers;

import android.os.Environment;
import java.io.File;

/**
 * Created by vmois on 10/4/17.
 */

public class Constants {
    private static File mainFolderFile;
    public final static String MAIN_FOLDER_NAME = "VladyslavMoisieienkov";
    public final static String NOTES_TABLE_NAME = "notes";
    public final static String MAIN_DATABASE_NAME = "DatabaseVladyslavMoisieienkov";
    public final static String[] NOTES_OPTIONS = {
            "Delete",
            "Edit",
            "Sort by title",
            "Sort by color",
            "Sort by image path"
    };

    public Constants() {
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mainFolderFile = new File(pictureFolder, MAIN_FOLDER_NAME);
    }

    public File getMainFolderFile() {
        return mainFolderFile;
    }
}
