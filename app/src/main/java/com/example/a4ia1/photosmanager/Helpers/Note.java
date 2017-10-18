package com.example.a4ia1.photosmanager.Helpers;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class Note {
    private String imagePath;
    private String title;
    private String text;
    private String color;

    public Note(String Title, String Text, String Color, String ImagePath) {
        this.imagePath = ImagePath;
        this.title = Title;
        this.text = Text;
        this.color = Color;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public String getColor() {
        return this.color;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}
