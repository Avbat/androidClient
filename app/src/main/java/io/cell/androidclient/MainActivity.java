package io.cell.androidclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Set;
import java.util.TreeSet;

import io.cell.androidclient.api.ApiFactory;
import io.cell.androidclient.api.habitat.HabitatApi;
import io.cell.androidclient.api.habitat.HabitatResponseHandlerFactory;
import io.cell.androidclient.model.Address;
import io.cell.androidclient.model.Cell;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private HabitatApi habitatApi;
    private HabitatResponseHandlerFactory handlerFactory;
    private Integer areaSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        habitatApi = new ApiFactory(this).getHabitatApi();
        handlerFactory = new HabitatResponseHandlerFactory(this);
        areaSize = getResources().getInteger(R.integer.areaSize);
    }

    public void showInfo(View view) {
        String currentAddressJson = getResources().getString(R.string.defaultCellAddress);
        Gson gson = new Gson();
        Address currentAddress = gson.fromJson(currentAddressJson, Address.class);
        habitatApi.getArea(currentAddress.getX(), currentAddress.getY(), areaSize).enqueue(handlerFactory.getAreaResponseHandler());
    }
}
