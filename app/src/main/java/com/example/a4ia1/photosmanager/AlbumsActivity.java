package com.example.a4ia1.photosmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumsActivity extends AppCompatActivity {

    private List<String> foldersList;
    private ListView listView;
    private ImageView addFolderButton;
    private File mainDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        // hide top bar
        getSupportActionBar().hide();

        // const for folder scan
        // foldersList = new ArrayList<>();
        final File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        final String mainFolderName = getString(R.string.main_folder_name);
        mainDir = new File(pictureFolder, mainFolderName);

        updateListAdapter();

        addFolderButton = (ImageView) findViewById(R.id.add_folder_icon);

        addFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddFolderButtonClick(v);
            }
        });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        updateListAdapter();
    }

    private void onAddFolderButtonClick(View view) {
        final AlertDialog.Builder addFolderDialog = new AlertDialog.Builder(AlbumsActivity.this);
        final String addFolderButtonLabel = getString(R.string.add_folder_button_label);
        final String addFolderTitle = getString(R.string.add_folder_title);
        final String alertFolderExist = getString(R.string.alert_folder_exists);
        final String alertFolderCreated = getString(R.string.alert_folder_created);
        final EditText input = new EditText(this);

        addFolderDialog.setTitle(addFolderTitle);
        addFolderDialog.setView(input);

        addFolderDialog.setNeutralButton(addFolderButtonLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String newFolderName = input.getText().toString();
                File tmpFolder = new File(mainDir, newFolderName);
                if(tmpFolder.exists()) {
                    Toast.makeText(AlbumsActivity.this, alertFolderExist, Toast.LENGTH_SHORT).show();
                    return;
                }
                tmpFolder.mkdir();
                updateListAdapter();
                Toast.makeText(AlbumsActivity.this, alertFolderCreated, Toast.LENGTH_SHORT).show();
            }
        });
        addFolderDialog.show();
    }

    private void updateListAdapter() {
        foldersList = new ArrayList<>();
        // get list of all folders in main folder
        for(File file: mainDir.listFiles()) {
            if(file.isDirectory()) {
                foldersList.add(file.getName());
            }
        }
        // get listview
        listView = (ListView) findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AlbumsActivity.this,
                R.layout.row_layout,
                R.id.row_layout_text,
                foldersList );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String folderName = foldersList.get(i);
                Intent intent = new Intent(AlbumsActivity.this, AlbumActivity.class);
                intent.putExtra("folderName", folderName);
                startActivity(intent);
            }
        });
    }
}
