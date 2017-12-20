package com.example.a4ia1.photosmanager.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a4ia1.photosmanager.Network.GetJson;
import com.example.a4ia1.photosmanager.Network.ImageData;
import com.example.a4ia1.photosmanager.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout  albumsButton;
    private LinearLayout  cameraButton;
    private Button     leftArrowButton;
    private Button    rightArrowButton;
    private LinearLayout collageButton;
    private LinearLayout networkButton;
    private LinearLayout notesButton;
    private List<String> foldersList;
    private ImageView smallImage;
    private List<Drawable> images;
    private int smallImagesCount;
    private int startSmallImageCount;
    private int currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // consts
        String[] baseSubFolders = new String[]{ "places", "people", "things" };
        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String mainFolderName = getString(R.string.main_folder_name);
        File mainDir = new File(pictureFolder, mainFolderName);
        foldersList = new ArrayList<>();

        // check if main and base sub folders exist
        // if not create them
        // START
        if (!mainDir.exists()) {
            mainDir.mkdir();
        }

        if(mainDir.isDirectory()) {
            for (String folderName : baseSubFolders) {
                File tmpFolder = new File(mainDir, folderName);
                if (!tmpFolder.exists()) {
                    tmpFolder.mkdir();
                }
            }
        }
        // END

        images = new ArrayList<>();
        smallImage = (ImageView) findViewById(R.id.smallImage);

        // set click event for AlbumsButton
        albumsButton = (LinearLayout) findViewById(R.id.albums_button);
        leftArrowButton = (Button) findViewById(R.id.left_arrow);
        rightArrowButton = (Button) findViewById(R.id.right_arrow);
        cameraButton = (LinearLayout) findViewById(R.id.camera_button);
        collageButton = (LinearLayout) findViewById(R.id.collage_button);
        networkButton = (LinearLayout) findViewById(R.id.network_button);
        notesButton = (LinearLayout) findViewById(R.id.notes_button);

        albumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlbumsButtonClick();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraButtonClick();
            }
        });

        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotesButtonClick();
            }
        });

        collageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCollageButtonClick();
            }
        });

        networkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NetworkActivity.class);
                startActivity(intent);
            }
        });


        GetJson getMinImagesJson = new GetJson();
        getMinImagesJson.setCustomObjectListener(new GetJson.GetJSONCustomListenerObject() {
            @Override
            public void onGetReady(List<ImageData> response) {
                if (response != null) {
                    smallImagesCount = response.size();
                    startSmallImageCount = 0;
                    for(ImageData data: response) {
                        new LoadImageTask().execute(LoadImageTask.URL_MIN + data.getImageName());
                    }

                }
            }
        });
        getMinImagesJson.execute(GetJson.URL_MIN);
        currentImage = 0;
        leftArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImage == 0) {
                    currentImage = images.size();
                }
                currentImage--;
                smallImage.setImageDrawable(images.get(currentImage));
            }
        });
        rightArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImage == images.size() - 1) {
                    currentImage = -1;
                }
                currentImage++;
                smallImage.setImageDrawable(images.get(currentImage));
            }
        });
    }

    public class LoadImageTask extends AsyncTask<String, Void, Void> {
        public static final String URL_MIN =
                "http://192.168.1.104:3000/min/";

        private Drawable loadedImage;

        public Drawable LoadImageFromWeb(String url) {
            try {
                InputStream inputStream = (InputStream) new URL(url).getContent();
                return Drawable.createFromStream(inputStream, "src name");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Void doInBackground(String... urls) {
            loadedImage = LoadImageFromWeb(urls[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startSmallImageCount++;
            images.add(loadedImage);
            if (startSmallImageCount >= smallImagesCount) {
                smallImage.setImageDrawable(images.get(0));
            }
        }
    }

    // event function for AlbumsButton
    private void onAlbumsButtonClick() {
        Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
        startActivity(intent);
    }

    private void onCameraButtonClick() {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    private void onNotesButtonClick() {
        Intent intent = new Intent(MainActivity.this, NotesActivity.class);
        startActivity(intent);
    }

    private void onCollageButtonClick() {
        Intent intent = new Intent(getApplicationContext(), ChooseCollage.class);
        startActivity(intent);
    }
}
