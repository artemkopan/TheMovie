package com.artemkopan.movie.util.glide;

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.17
 */

public class PosterImage implements ApiImageModel {

    private final String path;

    public PosterImage(String path) {this.path = path;}

    @Override
    public String requestUrl(int width, int height) {
        return "https://image.tmdb.org/t/p/w500/" + path;
    }
}
