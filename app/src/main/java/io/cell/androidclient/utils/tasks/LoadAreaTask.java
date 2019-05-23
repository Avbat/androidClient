package io.cell.androidclient.utils.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import io.cell.androidclient.api.ApiFactory;
import io.cell.androidclient.api.habitat.HabitatApi;
import io.cell.androidclient.model.Address;
import io.cell.androidclient.model.Area;
import io.cell.androidclient.model.Cell;
import io.cell.androidclient.utils.AreaBuilder;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoadAreaTask extends AsyncTask<Void, Integer, Area> {

    private Context context;
    private AreaBuilder builder;
    private HabitatApi habitatApi;


    public LoadAreaTask(Context context) {
        super();
        this.context = context;
        builder = new AreaBuilder(context);
        habitatApi = new ApiFactory(context).getHabitatApi();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Area area) {
        super.onPostExecute(area);
//        ((MainActivity) context).fillActivityArea();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Area area) {
        super.onCancelled(area);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Area doInBackground(Void... voids) {
        builder.setDefaultAreaSize()
                .setDefaultAddress();
        Address currentAddress = builder.getCurrentAddress();
        Set<Cell> cells = loadAreaCells(currentAddress.getX(), currentAddress.getY(), builder.getAreaSize());
        Map<String, Bitmap> images = loadImages(cells);
        return builder
                .setCanvas(cells)
                .setImageCache(images)
                .build();
    }

    private Set<Cell> loadAreaCells(Integer x, Integer y, Integer areaSize) {
        Set<Cell> cells = new TreeSet<>();
        try {
            cells.addAll(
                    Objects.requireNonNull(
                            habitatApi.getArea(x, y, areaSize)
                                    .execute()
                                    .body()));
        } catch (IOException e) {
            Log.e("Habitat", "The request failed.", e);
        } catch (NullPointerException e) {
            Log.e("Habitat", "The cell area could not be loaded.", e);
        }
        return cells;
    }

    private Map<String, Bitmap> loadImages(Set<Cell> cells) {
        Map<String, Bitmap> images = new HashMap<>();
        for (Cell cell : cells) {
            Response<ResponseBody> response = null;
            try {
                response = habitatApi.getImage(cell.getBackgroundImage()).execute();
            } catch (IOException e) {
                Log.e("Habitat", "The request failed.", e);
            }
            if (response!= null && response.isSuccessful()) {
                Bitmap backgroundImage = BitmapFactory.decodeStream(response.body().byteStream());
                images.put(cell.getBackgroundImage(), backgroundImage);
            } else {
                Log.e("Habitat", response.message());
            }
        }
        return images;
    }

}
