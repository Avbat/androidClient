package io.cell.androidclient.api.habitat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import io.cell.androidclient.R;
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

    public Callback<Set<Cell>> getAreaResponseHandler() {
        return new Callback<Set<Cell>>() {
            @Override
            public void onResponse(Call<Set<Cell>> call, Response<Set<Cell>> response) {
                if (response.isSuccessful()) {
                    TreeSet<Cell> cells = new TreeSet<>();
                    cells.addAll(response.body());
                    String message = "Cells: ";
                    for (Cell cell : cells) {
                        message += System.lineSeparator() + cell.getAddress().toString();
                    }
                    Toast.makeText(context,
                            message,
                            Toast.LENGTH_SHORT).show();
                    habitatApi.getImage("v1_18_18.jpg").enqueue(getImageResponseHandler());
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

    public Callback<ResponseBody> getImageResponseHandler() {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Bitmap backgroundImage = BitmapFactory.decodeStream(response.body().byteStream());
                    View centralView = ((Activity) context).findViewById(R.id.cell_3_2);
                    centralView.setBackground(new BitmapDrawable(context.getResources(), backgroundImage));
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
