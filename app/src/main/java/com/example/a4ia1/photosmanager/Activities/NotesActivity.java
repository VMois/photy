package com.example.a4ia1.photosmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.DatabaseManager;
import com.example.a4ia1.photosmanager.Helpers.Note;
import com.example.a4ia1.photosmanager.Adapters.NotesArrayAdapter;
import com.example.a4ia1.photosmanager.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotesActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseManager db;
    private ArrayList<Note> notesList;

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
    }

    // Positions
    // 0 - delete note
    // 1 - edit note (open NoteEditActivity)
    // 2 - sort by title
    // 3 - sort by color
    // 4 - sort by image path
    private void handleNoteOption(int which, Note selectedNote) {
        switch (which) {
            case 0:
                db.deleteNote(selectedNote.getId());
                renderNotesList(true);
                return;
            case 1:
                Intent intent = new Intent(NotesActivity.this, NoteEditActivity.class);
                intent.putExtra("note_id", selectedNote.getId());
                startActivity(intent);
                return;
            case 2:
                Collections.sort(notesList, new Comparator<Note>() {
                    @Override
                    public int compare(Note a, Note b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                renderNotesList(false);
                return;
            case 3:
                Collections.sort(notesList, new Comparator<Note>() {
                    @Override
                    public int compare(Note a, Note b) {
                        return a.getColor().compareTo(b.getColor());
                    }
                });
                renderNotesList(false);
                return;
            case 4:
                Collections.sort(notesList, new Comparator<Note>() {
                    @Override
                    public int compare(Note a, Note b) {
                        return a.getImagePath().compareTo(b.getImagePath());
                    }
                });
                renderNotesList(false);
                return;
            default:
        }
    }

    private void renderNotesList(boolean update) {
        // get all notes from database
        if (update) {
            notesList = db.getAllNotes();
        }

        // create our custom adapter
        NotesArrayAdapter adapter = new NotesArrayAdapter(
                NotesActivity.this,
                R.layout.notes_row_layout,
                notesList );

        // connect adapter to our listView to show notes
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                String dialogTitle = getString(R.string.note_options_dialog_title);
                alert.setTitle(dialogTitle);
                alert.setItems(Constants.NOTES_OPTIONS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Note selectedNote = notesList.get(i);
                        handleNoteOption(which, selectedNote);
                    }
                });
                alert.show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderNotesList(true);
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
