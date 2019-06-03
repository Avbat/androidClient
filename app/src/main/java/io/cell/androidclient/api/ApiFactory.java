package io.cell.androidclient.api;

import android.content.Context;

import org.androidannotations.annotations.EBean;

import java.util.concurrent.TimeUnit;

import io.cell.androidclient.R;
import io.cell.androidclient.api.habitat.HabitatApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Фабрика для формирования Rest API классов на основании {@link Retrofit}
 */
@EBean
public class ApiFactory {

    private Context context;

    ApiFactory(Context context) {
        this.context = context;
    }

    public HabitatApi getHabitatApi() {
        String url = context.getResources().getString(R.string.habitatUrl);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HabitatApi.class);
    }
}
