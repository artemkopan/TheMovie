package com.artemkopan.movie.util.glide.models;

import com.artemkopan.movie.Constants.Url;

import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.17
 */

public class YoutubeThumbnail implements ApiImageModel {

    private final String key;

    public YoutubeThumbnail(String key) {this.key = key;}

    @Override
    public String requestUrl(int width, int height) {
        return String.format(Locale.US, Url.YOUTUBE_IMG, key);
    }
}
