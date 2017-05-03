package com.artemkopan.movie.data.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;

@RealmClass
public class PostersItem implements RealmModel {

    @SerializedName("aspect_ratio")
    private double aspectRatio;

    @SerializedName("file_path")
    private String filePath;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}