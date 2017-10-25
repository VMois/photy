package com.example.a4ia1.photosmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a4ia1.photosmanager.Helpers.Constants;
import com.example.a4ia1.photosmanager.Helpers.DatabaseManager;
import com.example.a4ia1.photosmanager.R;

public class NoteEditActivity extends AppCompatActivity {

    private DatabaseManager db;

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
    }
}
