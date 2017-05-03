package com.artemkopan.movie.util.glide.models;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by Artem Kopan for TheMovie
 * 30.04.17
 */
public class ApiImageModelFactory implements ModelLoaderFactory<ApiImageModel, InputStream> {

    @Override
    public ModelLoader<ApiImageModel, InputStream> build(Context context, GenericLoaderFactory factories) {
        return new ApiImageUrlLoader(context);
    }

    @Override
    public void teardown() {

    }
}
