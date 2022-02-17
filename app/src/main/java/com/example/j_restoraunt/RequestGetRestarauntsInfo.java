package com.example.j_restoraunt;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestGetRestarauntsInfo {
    @SerializedName("label")
    public String label;

    @SerializedName("description")
    public String description;

    @SerializedName("label_photo")
    public String label_photo;

    @SerializedName("menu_photos")
    public ArrayList<String> menu;

    @SerializedName("photos")
    public ArrayList<String> photos;
}
