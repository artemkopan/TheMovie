package com.artemkopan.movie.data.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;

@RealmClass
public class VideoItem implements RealmModel {

    @SerializedName("site")
    private String site;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String videoId;

    @SerializedName("type")
    private String type;

    @SerializedName("key")
    private String key;

    public String getSite() {
        return site;
    }

    public String getName() {
        return name;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}