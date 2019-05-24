package io.cell.androidclient.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import io.cell.androidclient.R;
import io.cell.androidclient.model.Address;
import io.cell.androidclient.model.Area;
import io.cell.androidclient.model.Cell;

public class AreaBuilder {
    private Context context;

    private Integer areaSize;
    private Set<Cell> canvas = new TreeSet<>();
    private java.util.Map<String, Bitmap> imageCache = new HashMap<>();
    private Address currentAddress;
    private boolean loaded;

    public AreaBuilder(Context context) {
        this.context = context;
    }

    public Area build() {
        return Area.getInstance()
                .setAreaSize(areaSize)
                .setCurrentAddress(currentAddress)
                .setConvas(canvas)
                .setImageCache(imageCache)
                .setLoaded(loaded);
    }

    public Context getContext() {
        return context;
    }

    public AreaBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public AreaBuilder setAreaSize(Integer areaSize) {
        this.areaSize = areaSize;
        return this;
    }

    public AreaBuilder setDefaultAreaSize() {
        this.areaSize = context.getResources().getInteger(R.integer.areaSize);
        return this;
    }

    public Set<Cell> getCanvas() {
        return canvas;
    }

    public AreaBuilder setCanvas(Set<Cell> canvas) {
        this.canvas = canvas;
        return this;
    }

    public Map<String, Bitmap> getImageCache() {
        return imageCache;
    }

    public AreaBuilder setImageCache(Map<String, Bitmap> imageCache) {
        this.imageCache = imageCache;
        return this;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public AreaBuilder setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
        return this;
    }

    public AreaBuilder setDefaultAddress() {
        String defaultAddressJson = context.getResources().getString(R.string.defaultCellAddress);
        Gson gson = new Gson();
        Address defaultAddress = gson.fromJson(defaultAddressJson, Address.class);
        this.currentAddress = defaultAddress;
        return this;
    }

    public Integer getAreaSize() {
        return context.getResources().getInteger(R.integer.areaSize);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public AreaBuilder setLoaded(boolean loaded) {
        this.loaded = loaded;
        return this;
    }
}

