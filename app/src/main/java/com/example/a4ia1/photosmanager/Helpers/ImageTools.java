package com.example.a4ia1.photosmanager.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vmois on 11/1/17.
 */

public class ImageTools {
    public static Bitmap fromByteToBitmap(byte[] rawImageData) {
        return BitmapFactory.decodeByteArray(rawImageData, 0, rawImageData.length);
    }
    public static Bitmap scaleBitmap(Bitmap unscaledBitmap, int width, int height) {
        return Bitmap.createScaledBitmap(unscaledBitmap, width, height, false);
    }
    public static Bitmap rotate(Bitmap originalBitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(originalBitmap, 0, 0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(), matrix, true);
    }

    public static void saveOnDisk(String path, byte[] data) {
        // create bitmap from byte array
        Bitmap bitmapToSave = BitmapFactory.decodeByteArray(data, 0, data.length);

        // return to proper state
        bitmapToSave = rotate(bitmapToSave, -90);

        try {
            FileOutputStream fs = new FileOutputStream(path);
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, fs);
            fs.close();
        } catch (IOException err) {
            Log.e("[!] Bad photo save", err.toString());
        }
    }
}
