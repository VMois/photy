package com.example.a4ia1.photosmanager.Helpers;

/**
 * Created by 4ia1 on 2017-10-05.
 */
public class Note {
    private int id;
    private String title;
    private String text;
    private String color;
    private String imagePath;

    public Note(int Id, String Title, String Text, String Color, String ImagePath) {
        this.id = Id;
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

    public int getId() {
        return this.id;
    }
}
