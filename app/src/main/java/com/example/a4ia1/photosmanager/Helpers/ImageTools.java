package com.example.a4ia1.photosmanager.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
}
