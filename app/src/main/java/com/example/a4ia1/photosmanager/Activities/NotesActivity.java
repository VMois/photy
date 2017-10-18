package com.example.a4ia1.photosmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.DatabaseManager;
import com.example.a4ia1.photosmanager.Helpers.Note;
import com.example.a4ia1.photosmanager.Helpers.NotesArrayAdapter;
import com.example.a4ia1.photosmanager.R;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // get listview from activity_notes.xml
        listView = (ListView) findViewById(R.id.notes_listview);

        // create database, version 1
        db = new DatabaseManager(
                NotesActivity.this,
                Constants.MAIN_DATABASE_NAME,
                null,
                1
        );

        // get all notes from database
        ArrayList<Note> notesList = db.getAllNotes();

        // create our custom adapter
        NotesArrayAdapter adapter = new NotesArrayAdapter(
                NotesActivity.this,
                R.layout.notes_row_layout,
                notesList );

        // connect adapter to our listView to show notes
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
