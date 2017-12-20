package com.example.a4ia1.photosmanager.Network;

/**
 * Created by vmois on 12/20/17.
 */

public class ImageData {
    private String imageName;
    private String imageSaveTime;

    public ImageData(String imageName, String imageSaveTime) {
        this.imageName = imageName;
        this.imageSaveTime = imageSaveTime;
    }
    public String getImageName() {
        return imageName;
    }

    public String getImageSaveTime() {
        return imageSaveTime;
    }
}
