package com.example.a4ia1.photosmanager.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.NOTES_TABLE_NAME
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'text' TEXT, 'color' TEXT, 'image_path' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop old table
        db.execSQL("DROP TABLE IF EXISTS " + Constants.NOTES_TABLE_NAME);

        // increase database version
        db.setVersion(db.getVersion() + 1);
        // create new table
        onCreate(db);
    }

    public boolean insert(String title, String text, String color, String imagePath){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("text", text);
        contentValues.put("color", color);
        contentValues.put("image_path", imagePath);

        db.insertOrThrow(Constants.NOTES_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<Note> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes= new ArrayList<>();
        // craft query
        Cursor result = db.rawQuery("SELECT * FROM " + Constants.NOTES_TABLE_NAME , null);

        // iterate over results
        while(result.moveToNext()){
            Note newNote = new Note(
                    result.getInt(result.getColumnIndex("_id")),
                    result.getString(result.getColumnIndex("title")),
                    result.getString(result.getColumnIndex("text")),
                    result.getString(result.getColumnIndex("color")),
                    result.getString(result.getColumnIndex("image_path"))
            );
            notes.add(newNote);
        }
        return notes;
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String convertedId = String.valueOf(id);
        return db.delete(Constants.NOTES_TABLE_NAME, "_id = ? ", new String[]{convertedId});
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        String convertedId = String.valueOf(note.getId());
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("text", note.getText());
        contentValues.put("color", note.getColor());
        return db.update(Constants.NOTES_TABLE_NAME, contentValues, "_id = ? ", new String[]{convertedId});
    }

    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String convertedId = String.valueOf(id);
        String query = "SELECT * FROM " + Constants.NOTES_TABLE_NAME + " WHERE _id = " + convertedId;
        Cursor result = db.rawQuery(query, null);

        // return empty Note with id -1 to show that
        // no Note with id not found
        Note objectToReturn = new Note(
                -1, "","","",""
        );

        if (result.moveToFirst()) {
            objectToReturn = new Note(
                    result.getInt(result.getColumnIndex("_id")),
                    result.getString(result.getColumnIndex("title")),
                    result.getString(result.getColumnIndex("text")),
                    result.getString(result.getColumnIndex("color")),
                    result.getString(result.getColumnIndex("image_path"))
            );
        }
        return objectToReturn;
    }
}
