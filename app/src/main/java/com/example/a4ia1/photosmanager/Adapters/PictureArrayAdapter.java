package com.example.a4ia1.photosmanager.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4ia1.photosmanager.Helpers.DrawerMenuItem;
import com.example.a4ia1.photosmanager.R;

import java.util.ArrayList;

/**
 * Created by vmois on 11/24/17.
 */

public class PictureArrayAdapter extends ArrayAdapter{
        private ArrayList<DrawerMenuItem> _list;

        public PictureArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DrawerMenuItem> objects) {
            super(context, resource, objects);
            this._list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.picture_row_layout, null);

            TextView idTextView = convertView.findViewById(R.id.picture_drawer_textview);
            ImageView iv = convertView.findViewById(R.id.picture_drawer_image);

            DrawerMenuItem currentMenuItem = this._list.get(position);
            idTextView.setText(currentMenuItem.getMenuOptionText());

            iv.setImageResource(currentMenuItem.getDrawableId());

            return convertView;
        }
}
