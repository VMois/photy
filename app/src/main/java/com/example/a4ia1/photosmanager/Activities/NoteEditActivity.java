package com.example.a4ia1.photosmanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.DatabaseManager;
import com.example.a4ia1.photosmanager.Helpers.Note;
import com.example.a4ia1.photosmanager.R;

public class NoteEditActivity extends AppCompatActivity {

    private DatabaseManager db;
    private Button saveButton;
    private EditText titleEditText;
    private EditText textEditText;
    private Note currentNote;
    private String currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        Bundle bundle = getIntent().getExtras();
        int noteId = bundle.getInt("note_id", 0);
        db = new DatabaseManager(
                NoteEditActivity.this,
                Constants.MAIN_DATABASE_NAME,
                null,
                1
        );

        currentNote = db.getNoteById(noteId);
        // check if note is found
        // if not throw exception
        if (currentNote.getId() == -1) {
            throw new RuntimeException("Note with given ID not found!");
        }
        currentColor = currentNote.getColor();
        titleEditText = (EditText) findViewById(R.id.note_edit_title);
        titleEditText.setText(currentNote.getTitle());
        titleEditText.setTextColor(Color.parseColor(currentColor));

        textEditText = (EditText) findViewById(R.id.note_edit_text);
        textEditText.setText(currentNote.getText());
        textEditText.setTextColor(Color.parseColor(currentColor));

        saveButton = (Button) findViewById(R.id.note_edit_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick(v);
            }
        });
    }

    private void onSaveButtonClick(View v) {
        String title = titleEditText.getText().toString();
        String text = textEditText.getText().toString();
        Note newNote = new Note(
                currentNote.getId(),
                title,
                text,
                currentColor,
                currentNote.getImagePath()
        );
        db.updateNote(newNote);
        Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
        startActivity(intent);
    }
}
