package io.cell.androidclient.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.cell.androidclient.R;
import io.cell.androidclient.model.Address;
import io.cell.androidclient.model.Area;
import io.cell.androidclient.model.Cell;

@EBean
public class AreaBuilder{
    private Context context;

    private Integer areaSize;
    private Map<Address, Cell> canvas = new TreeMap<>();
    private java.util.Map<String, Bitmap> imageCache = new HashMap<>();
    private Address currentAddress;
    private boolean loaded;

    public AreaBuilder(Context context) {
        this.context = context;
    }

    public Area build() {
        return new Area()
                .setAreaSize(areaSize)
                .setCurrentAddress(currentAddress)
                .setCanvas(canvas)
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

    public Map<Address, Cell> getCanvas() {
        return canvas;
    }

    public AreaBuilder setCanvas(Set<Cell> canvas) {
        this.canvas.clear();
        for (Cell cell : canvas) {
            this.canvas.put(cell.getAddress(), cell);
        }
        return this;
    }

    public AreaBuilder setCanvas(Map<Address, Cell> canvas) {
        this.canvas = canvas;
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
        this.currentAddress = getDefaultAddress();
        return this;
    }

    public Address getDefaultAddress() {
        String defaultAddressJson = context.getResources().getString(R.string.defaultCellAddress);
        Gson gson = new Gson();
        return gson.fromJson(defaultAddressJson, Address.class);
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

