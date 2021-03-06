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
    public final static String[] MINIATURES_OPTIONS = {
            "Show",
            "Delete",
            "Save"
    };
    public final static String[] SPINNER_OPTIONS = {
            "[Empty]", // for test purpose
            "Save last",
            "Save all",
            "Delete all"
    };

    public final static String[] COLLAGE_PHOTO_DIALOG_OPTIONS = {
        "Gallery",
        "Camera"
    };

    public final static String[] COLLAGE_DIALOG_OPTIONS = {
            "Save collage",
    };

    public final static String FOLDER_NAME_FOR_COLLAGES = "collages";

    public final static String FONTS_DEFAULT_PREVIEW_TEXT = "Żźaąęóńuy";

    public Constants() {
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mainFolderFile = new File(pictureFolder, MAIN_FOLDER_NAME);
    }

    public File getMainFolderFile() {
        return mainFolderFile;
    }
}
