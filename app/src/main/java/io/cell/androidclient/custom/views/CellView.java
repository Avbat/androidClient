package io.cell.androidclient.custom.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import io.cell.androidclient.model.Address;

public class CellView extends View {

    private Address address;

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Address getAddress() {
        return address;
    }

    public CellView setAddress(Address address) {
        this.address = address;
        return this;
    }
}
