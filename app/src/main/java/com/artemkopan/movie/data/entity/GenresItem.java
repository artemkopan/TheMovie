package com.artemkopan.movie.data.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;

@RealmClass
public class GenresItem implements RealmModel {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "GenresItem{" +
                "name = '" + name + '\'' +
                ",id = '" + id + '\'' +
                "}";
    }
}