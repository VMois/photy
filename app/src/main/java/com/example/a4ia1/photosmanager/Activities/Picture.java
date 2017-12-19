package com.example.a4ia1.photosmanager.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a4ia1.photosmanager.Adapters.PictureArrayAdapter;
import com.example.a4ia1.photosmanager.Helpers.DrawerMenuItem;
import com.example.a4ia1.photosmanager.Helpers.PreviewText;
import com.example.a4ia1.photosmanager.Network.NetworkStatus;
import com.example.a4ia1.photosmanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Picture extends AppCompatActivity {

    private ImageView mainImage;
    private ImageView scaleIconButton;
    private ImageView deleteButton;
    private File currentFile;
    private Bitmap originalBitmap;
    private Bitmap imageBitmap;
    private int originalWidth;
    private int originalHeight;
    private Point size;
    private RelativeLayout mainLayout;
    private ArrayList<PreviewText> previewTextList;
    private ProgressDialog pDialog;

    // 0 - big, 1 - middle, 2 - small;
    private int scaleType = 1;
    final private int bigParams = RelativeLayout.LayoutParams.MATCH_PARENT;
    final private int middleParams = RelativeLayout.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // hide top bar
        getSupportActionBar().hide();

        // get display sizes
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString("imagePath").toString();
        currentFile = new File(imagePath);
        originalBitmap = betterImageDecode(imagePath);
        originalWidth = originalBitmap.getWidth();
        originalHeight = originalBitmap.getHeight();
        imageBitmap = Bitmap.createScaledBitmap(originalBitmap, size.x, size.y, true);

        mainImage = (ImageView) findViewById(R.id.main_image);
        mainImage.setImageBitmap(imageBitmap);

        scaleIconButton = (ImageView) findViewById(R.id.scale_icon);
        scaleIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScaleButtonClick(v);
            }
        });

        deleteButton = (ImageView) findViewById(R.id.delete_image_icon);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteImageButtonClick(v);
            }
        });
        ArrayList<DrawerMenuItem> PICTURE_MENU_ITEMS = new ArrayList<>(
                Arrays.asList(
                        new DrawerMenuItem(R.mipmap.ic_fonts, "Fonts"),
                        new DrawerMenuItem(R.mipmap.ic_upload, "Upload"),
                        new DrawerMenuItem(R.mipmap.ic_share, "Share")
                )
        );
        PictureArrayAdapter menuAdapter = new PictureArrayAdapter(
                getApplicationContext(),
                R.layout.picture_row_layout,
                PICTURE_MENU_ITEMS );
        ListView drawerListView = (ListView) findViewById(R.id.picture_drawer_listview);
        drawerListView.setAdapter(menuAdapter);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    // fonts choose
                    case 0:
                        Intent lettersIntent = new Intent(getApplicationContext(), LettersActivity.class);
                        startActivityForResult(lettersIntent, 300);
                        break;

                    // upload
                    case 1:
                        NetworkStatus net = new NetworkStatus(getApplicationContext());
                        boolean isNetConnected = net.isConnected();
                        if(!isNetConnected) {
                            AlertDialog alertDialog = new AlertDialog.Builder(Picture.this).create();
                            alertDialog.setTitle("Network Problem");
                            alertDialog.setMessage("No Internet!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            break;
                        }
                        break;
                    // share
                    case 2:
                        Uri uri = Uri.parse("file://" + currentFile.getAbsolutePath());
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.setType("image/jpeg");
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(share, "Share image File"));
                        break;
                }
            }
        });

        mainLayout = (RelativeLayout) findViewById(R.id.picture_main_layout);
        previewTextList = new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // letters - 300
        switch (requestCode) {
            case 300:
                if (data == null) return;
                Bundle extras = data.getExtras();
                String fontName = (String) extras.get("fontName");
                String text = (String) extras.get("text");
                int textColorBase = (int) extras.get("colorBase");
                int textColorStroke = (int) extras.get("colorStroke");
                Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/" + fontName);
                float startY = mainImage.getHeight() / 8;
                float startX = mainImage.getX() + 250;
                PreviewText previewText = new PreviewText(
                        getApplicationContext(),
                        text,
                        textColorBase,
                        textColorStroke,
                        tf,
                        startX,
                        startY);
                mainLayout.addView(previewText);
                previewText.bringToFront();
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(previewText.getTextWidth(), previewText.getTextHeight());
                previewText.setLayoutParams(layout);
                previewText.setX(startX);
                previewText.setY(startY);
                previewTextList.add(previewText);
                break;
        }
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    private void onScaleButtonClick(View view) {
        // params = new RelativeLayout.LayoutParams(bigParams, bigParams);
        switch (scaleType) {
            case 0:
                // params = new RelativeLayout.LayoutParams(bigParams, bigParams);
                imageBitmap = Bitmap.createScaledBitmap(originalBitmap, size.x, size.y, true);
                break;
            case 1:
                imageBitmap = Bitmap.createScaledBitmap(originalBitmap, originalWidth, originalWidth, true);
                // params = new RelativeLayout.LayoutParams(middleParams, middleParams);
                break;
            case 2:
                // params = new RelativeLayout.LayoutParams(middleParams, middleParams);
                imageBitmap = Bitmap.createScaledBitmap(originalBitmap, (int)(mainImage.getWidth()*0.5), (int)(mainImage.getHeight()*0.5), true);
                break;
            default:
                break;
        }
        // mainImage.setLayoutParams(params);
        mainImage.setImageBitmap(imageBitmap);
        scaleType++;
        if (scaleType > 2) {
            scaleType = 0;
        }
    }

    private void onDeleteImageButtonClick(View view) {
        String alertDeletedFolder = getString(R.string.alert_deleted_folder);
        String alertDeletedFolderNotExist = getString(R.string.alert_deleted_folder_not_exist);
        if(!currentFile.exists()) {
            Toast.makeText(getApplicationContext(), alertDeletedFolderNotExist, Toast.LENGTH_SHORT).show();
            return;
        }
        currentFile.delete();
        Toast.makeText(getApplicationContext(), alertDeletedFolder, Toast.LENGTH_SHORT).show();
        Picture.this.finish();
    }
}
