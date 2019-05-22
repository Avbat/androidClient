package io.cell.androidclient.api;

import android.content.Context;

import io.cell.androidclient.R;
import io.cell.androidclient.api.habitat.HabitatApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    Context context;


    public ApiFactory(Context context) {
        this.context = context;
    }

    public HabitatApi getHabitatApi() {
        String url = context.getResources().getString(R.string.habitatUrl);
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HabitatApi.class);
    }
}
