package io.cell.androidclient.model;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Area {
    private Integer areaSize;
    private Set<Cell> convas = new TreeSet<>();
    private java.util.Map<String, Bitmap> imageCache = new HashMap<>();
    private Address currentAddress;

    public Integer getAreaSize() {
        return areaSize;
    }

    public Area setAreaSize(Integer areaSize) {
        this.areaSize = areaSize;
        return this;
    }

    public Set<Cell> getConvas() {
        return convas;
    }

    public Area setConvas(Set<Cell> convas) {
        this.convas = convas;
        return this;
    }

    public Map<String, Bitmap> getImageCache() {
        return imageCache;
    }

    public Area setImageCache(Map<String, Bitmap> imageCache) {
        this.imageCache = imageCache;
        return this;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public Area setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
        return this;
    }
}
