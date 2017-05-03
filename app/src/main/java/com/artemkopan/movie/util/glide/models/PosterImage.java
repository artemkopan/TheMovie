package com.artemkopan.movie.util.glide.models;

import com.artemkopan.movie.Constants.Url;

import java.util.Locale;

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.17
 */

public class PosterImage implements ApiImageModel {

    private final String path;

    public PosterImage(String path) {this.path = path;}

    @Override
    public String requestUrl(int width, int height) {
        return String.format(Locale.US, Url.TMDB_IMG, path);
    }
}
