package com.artemkopan.movie.util.glide.models;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.17
 */

public class ApiImageUrlLoader extends BaseGlideUrlLoader<ApiImageModel> {

    ApiImageUrlLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(ApiImageModel model, int width, int height) {
        return model.requestUrl(width, height);
    }
}