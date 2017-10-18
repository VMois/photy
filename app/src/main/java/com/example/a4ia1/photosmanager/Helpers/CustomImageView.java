package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Activities.Picture;
import com.example.a4ia1.photosmanager.R;

import java.io.File;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class CustomImageView extends ImageView implements View.OnLongClickListener, ImageView.OnClickListener {

    private DatabaseManager db;
    private String chosenColor;
    private String imagePath;

    private EditText titleEditText;
    private EditText textEditText;
    private TextView titleTextView;
    private TextView textTextView;

    public CustomImageView(Context context, File CurrentImage, DatabaseManager Db) {
        super(context);
        this.imagePath = CurrentImage.getAbsolutePath();
        this.db = Db;

        this.setScaleType(ScaleType.CENTER_CROP);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), Picture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("imagePath", imagePath);
        view.getContext().startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        final View customView = View.inflate(view.getContext(), R.layout.note_add, null);
        alert.setView(customView);
        alert.setTitle("Add new note");

        final Button btRedColor = customView.findViewById(R.id.noteRedButton);
        final Button btBlueColor = customView.findViewById(R.id.noteBlueButton);
        final Button btGreenColor = customView.findViewById(R.id.noteGreenButton);

        // get elements
        titleTextView = customView.findViewById(R.id.title_textview);
        textTextView = customView.findViewById(R.id.text_textview);
        titleEditText = customView.findViewById(R.id.title_edit_text);
        textEditText = customView.findViewById(R.id.text_edit_text);

        // set base color (blue)
        int baseColor = ((ColorDrawable) btBlueColor.getBackground()).getColor();
        setColorForDialog(baseColor);

        // add listener
        // TODO: Refactor in future (switch or external library for color pick)
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
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String title = titleEditText.getText().toString();
                String text = textEditText.getText().toString();
                db.insert(title, text, chosenColor, imagePath);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
        return true;
    }

    private void onColorChoose(View view) {
        // get color in int
        int intColor = ((ColorDrawable) view.getBackground()).getColor();
        setColorForDialog(intColor);
    }

    private void setColorForDialog(int color) {
        // update color of text to inform user
        titleEditText.setTextColor(color);
        titleTextView.setTextColor(color);
        textTextView.setTextColor(color);

        // convert color from int format to HEX -> to save in db
        chosenColor = String.format("#%06X", (0xFFFFFF & color));
    }
}
