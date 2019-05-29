package io.cell.androidclient.utils.cache;

import android.graphics.Bitmap;

import java.util.Map;

public interface ImageCache {

    Bitmap getImage(String name);

    Bitmap putImage(String name, Bitmap image);

    Map<String, Bitmap> getCache();

}
