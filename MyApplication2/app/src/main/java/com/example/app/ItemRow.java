package com.example.app;

import android.graphics.drawable.Drawable;

/**
 * Created by Анастасия on 01.04.14.
 */
public class ItemRow {
    String itemName;
    Drawable icon;

    public ItemRow(String itemName, Drawable icon) {
        super();
        this.itemName = itemName;
        this.icon = icon;

    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}
