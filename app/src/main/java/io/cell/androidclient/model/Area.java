package io.cell.androidclient.model;

import java.util.Map;
import java.util.TreeMap;

public class Area{

    private Integer areaSize;
    private Map<Address, Cell> canvas = new TreeMap<>();
    private Address currentAddress;
    private Boolean loaded = false;

    public Integer getAreaSize() {
        return areaSize;
    }

    public Area setAreaSize(Integer areaSize) {
        this.areaSize = areaSize;
        return this;
    }

    public Map<Address, Cell> getCanvas() {
        return canvas;
    }

    public Area setCanvas(Map<Address, Cell> canvas) {
        this.canvas = canvas;
        return this;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public Area setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
        return this;
    }

    public Boolean isLoaded() {
        return loaded;
    }

    public Area setLoaded(Boolean loaded) {
        this.loaded = loaded;
        return this;
    }

    public Boolean getLoaded() {
        return loaded;
    }
}
