package com.artemkopan.movie.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;

@RealmClass
public class Videos implements RealmModel {

    @SerializedName("results")
    private RealmList<VideoItem> results;

    public RealmList<VideoItem> getResults() {
        return results;
    }
}