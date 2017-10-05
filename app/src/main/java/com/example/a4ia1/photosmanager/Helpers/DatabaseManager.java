package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE test1 (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'a' TEXT, 'b' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop old table
        db.execSQL("DROP TABLE IF EXISTS tabela1");
        // create new table
        onCreate(db);
    }
}
