package com.example.j_restoraunt;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestGetRestarauntsInfoBody {
    private String restaraunt_label;

    public RequestGetRestarauntsInfoBody(String restaraunt_label) {
        this.restaraunt_label = restaraunt_label;
    }
}
