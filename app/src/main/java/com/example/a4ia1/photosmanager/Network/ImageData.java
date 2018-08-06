package com.example.a4ia1.photosmanager.Network;

/**
 * Created by vmois on 12/20/17.
 */

public class ImageData {
    private String imageName;
    private String imageSaveTime;
    private int imageSize;

    public ImageData(String imageName, String imageSaveTime, int size) {
        this.imageName = imageName;
        this.imageSaveTime = imageSaveTime;
        this.imageSize = size;
    }
    public String getImageName() {
        return imageName;
    }

    public String getImageSaveTime() {
        return imageSaveTime;
    }

    public int getImageSize() {
        return imageSize;
    }
}
