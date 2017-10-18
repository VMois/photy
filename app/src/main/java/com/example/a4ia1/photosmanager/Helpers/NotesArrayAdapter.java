package com.example.a4ia1.photosmanager.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.R;

import java.util.ArrayList;

/**
 * Created by vmois on 10/18/17.
 */

public class NotesArrayAdapter extends ArrayAdapter {
    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;

    public NotesArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        this._list = objects;
        this._context = context;
        this._resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.notes_row_layout, null);

        // set number of note in list
        TextView idTextView = (TextView) convertView.findViewById(R.id.note_id_textview);
        idTextView.setText(String.valueOf(position + 1));

        // get current Note object
        Note currentNote = this._list.get(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.note_title_textview);
        titleTextView.setText(currentNote.getTitle());
        titleTextView.setTextColor(Color.parseColor(currentNote.getColor()));

        TextView textTextView = (TextView) convertView.findViewById(R.id.note_text_textview);
        textTextView.setText(currentNote.getText());

        // set debug log for future use
        ImageView iv1 = (ImageView) convertView.findViewById(R.id.note_base_image);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click on note", "Pos:" + position);
            }
        });

        return convertView;
    }
}
