package com.example.a4ia1.photosmanager.Helpers;

/**
 * Created by vmois on 11/24/17.
 */

public class DrawerMenuItem {
    private int drawableId;
    private String menuOptionText;

    public DrawerMenuItem (int drawableId, String menuOptionText) {
        this.drawableId = drawableId;
        this.menuOptionText = menuOptionText;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getMenuOptionText() {
        return menuOptionText;
    }
}
