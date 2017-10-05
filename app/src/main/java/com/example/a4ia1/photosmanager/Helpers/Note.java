package com.example.a4ia1.photosmanager.Helpers;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class Note {
    private String imagePath;
    private String title;
    private String text;
    private String color;

    public Note(String ImagePath, String Title, String Text, String Color) {
        this.imagePath = ImagePath;
        this.title = Title;
        this.text = Text;
        this.color = Color;
    }
}
