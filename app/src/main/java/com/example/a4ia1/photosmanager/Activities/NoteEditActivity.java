package com.example.a4ia1.photosmanager.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.DatabaseManager;
import com.example.a4ia1.photosmanager.Helpers.Note;
import com.example.a4ia1.photosmanager.R;

public class NoteEditActivity extends AppCompatActivity {

    private DatabaseManager db;
    private EditText titleEditText;
    private EditText textEditText;
    private Note currentNote;
    private String currentColor;
    private TextView titleTextView;
    private TextView textTextView;

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

        textEditText = (EditText) findViewById(R.id.note_edit_text);
        textEditText.setText(currentNote.getText());

        titleTextView = (TextView) findViewById(R.id.title_textview);
        textTextView = (TextView) findViewById(R.id.text_textview);

        Button btRedColor = (Button) findViewById(R.id.noteRedButton);
        Button btBlueColor = (Button) findViewById(R.id.noteBlueButton);
        Button btGreenColor = (Button) findViewById(R.id.noteGreenButton);

        btRedColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorChoose(v);
            }
        });
        btBlueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorChoose(v);
            }
        });
        btGreenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorChoose(v);
            }
        });

        Button saveButton = (Button) findViewById(R.id.note_edit_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick(v);
            }
        });

        // set color on init
        setColorForEdit(Color.parseColor(currentColor));
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
        this.finish();
    }

    private void onColorChoose(View view) {
        int intColor = ((ColorDrawable) view.getBackground()).getColor();
        setColorForEdit(intColor);
    }

    private void setColorForEdit(int color) {
        // update color of text to inform user
        titleEditText.setTextColor(color);
        titleTextView.setTextColor(color);
        textTextView.setTextColor(color);

        // convert color from int format to HEX -> to save in db
        currentColor = String.format("#%06X", (0xFFFFFF & color));
    }
}
