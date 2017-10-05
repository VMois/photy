package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private Constants constants;

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        constants = new Constants();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + constants.getNotesTableName()
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'text' TEXT, 'color' TEXT, 'image_path' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop old table
        db.execSQL("DROP TABLE IF EXISTS " + constants.getMainDatabaseName());

        // increase database version
        db.setVersion(db.getVersion() + 1);
        // create new table
        onCreate(db);
    }
}
