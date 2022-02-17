package com.example.j_restoraunt;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestGetRestaraunts {
    @SerializedName("items")
    public ArrayList<ArrayList<String >> items;
}
