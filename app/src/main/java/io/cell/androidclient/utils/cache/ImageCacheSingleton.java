package io.cell.androidclient.utils.cache;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class ImageCacheSingleton implements ImageCache {
    private static final ImageCacheSingleton ourInstance = new ImageCacheSingleton();

    public static ImageCacheSingleton getInstance() {
        return ourInstance;
    }

    private ImageCacheSingleton() {
    }

    private Map<String, Bitmap> cache = new HashMap<>();

    @Override
    public Bitmap getImage(String name) {
        return cache.get(name);
    }

    @Override
    public Bitmap putImage(String name, Bitmap image) {
        return cache.put(name, image);
    }

    @Override
    public Map<String, Bitmap> getCache() {
        return cache;
    }
}
