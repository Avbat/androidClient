package io.cell.androidclient.api.habitat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import io.cell.androidclient.api.ApiFactory;
import io.cell.androidclient.model.Cell;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitatResponseHandlerFactory {

    private Context context;
    private HabitatApi habitatApi;

    public HabitatResponseHandlerFactory(Context context) {
        this.context = context;
        this.habitatApi = new ApiFactory(context).getHabitatApi();
    }

    public Callback<Set<Cell>> getAreaResponseHandler(final Set<Cell> cells) {
        return new Callback<Set<Cell>>() {
            @Override
            public void onResponse(Call<Set<Cell>> call, Response<Set<Cell>> response) {
                if (response.isSuccessful()) {
                    cells.addAll(response.body());
                } else {
                    Log.e("Error response: ", response.message());
                    Toast.makeText(context,
                            response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Set<Cell>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        };
    }

    public Callback<ResponseBody> getLoadImageResponseHandler(final Cell cell, final Map<String, Bitmap> imageCache) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Bitmap backgroundImage = BitmapFactory.decodeStream(response.body().byteStream());
                    imageCache.put(cell.getBackgroundImage(), backgroundImage);
                } else {
                    Log.e("Error response: ", response.message());
                    Toast.makeText(context,
                            response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        };
    }
}
