package com.example.a4ia1.photosmanager.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Helpers.Note;
import com.example.a4ia1.photosmanager.R;
import java.util.ArrayList;

/**
 * Created by vmois on 10/18/17.
 */

public class NotesArrayAdapter extends ArrayAdapter {
    private ArrayList<Note> _list;

    public NotesArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        this._list = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.notes_row_layout, null);

        // set number of notes in list
        TextView idTextView = convertView.findViewById(R.id.note_id_textview);

        // get current Note object
        Note currentNote = this._list.get(position);
        idTextView.setText(String.valueOf(currentNote.getId()));

        // cast is not needed here :)
        TextView titleTextView = convertView.findViewById(R.id.note_title_textview);
        titleTextView.setText(currentNote.getTitle());

        titleTextView.setTextColor(Color.parseColor(currentNote.getColor()));

        TextView textTextView = convertView.findViewById(R.id.note_text_textview);
        textTextView.setText(currentNote.getText());

        return convertView;
    }
}
