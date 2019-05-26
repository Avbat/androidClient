package io.cell.androidclient.utils.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
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

import static io.cell.androidclient.utils.tasks.Errors.LOAD_CELL_REQUEST_FAILED;
import static io.cell.androidclient.utils.tasks.Errors.LOAD_IMAGE_REQUEST_FAILED;
import static io.cell.androidclient.utils.tasks.Errors.LOAD_SERVER_UNAVALABLE;

public class AreaLoader extends AsyncTaskLoader<Area> {

    private final String TAG = getClass().getSimpleName();
    private AreaBuilder builder;
    private HabitatApi habitatApi;
    private Area area;
    private boolean errors = false;
    private String errorMessage = "";

    public AreaLoader(Context context) {
        super(context);
        this.area = Area.getInstance();
        builder = new AreaBuilder(context);
        habitatApi = new ApiFactory(context).getHabitatApi();
    }

    @Nullable
    @Override
    public Area loadInBackground() {
        errors = false;
        errorMessage = "";
        builder.setDefaultAreaSize()
                .setDefaultAddress();
        Address currentAddress = builder.getCurrentAddress();
        Set<Cell> cells = loadAreaCells(currentAddress.getX(), currentAddress.getY(), builder.getAreaSize());
        Map<String, Bitmap> images = loadImages(cells);
        return builder
                .setCanvas(cells)
                .setImageCache(images)
                .setLoaded(!errors)
                .build();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (area != null
                && area.isLoaded()
                && !area.getConvas().isEmpty()
                && area.getImageCache().size() >= area.getConvas().size()) {
            cancelLoad();
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public void deliverResult(Area area) {
        super.deliverResult(area);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    private Set<Cell> loadAreaCells(Integer x, Integer y, Integer areaSize) {
        Set<Cell> cells = new TreeSet<>();
        try {
            Response<Set<Cell>> response = habitatApi.getArea(x, y, areaSize).execute();
            if (response.isSuccessful()) {
                cells.addAll(Objects.requireNonNull(response.body()));
            }else {
                errors = true;
                errorMessage = LOAD_SERVER_UNAVALABLE.getDescription();
                Log.e(TAG, response.message());
            }
        } catch (IOException e) {
            errors = true;
            errorMessage = LOAD_SERVER_UNAVALABLE.getDescription();
            Log.e(TAG, "The request failed.", e);
        } catch (NullPointerException e) {
            errors = true;
            errorMessage = LOAD_CELL_REQUEST_FAILED.getDescription();
            Log.e(TAG, "The cell area could not be loaded.", e);
        }
        return cells;
    }

    private Map<String, Bitmap> loadImages(Set<Cell> cells) {
        Map<String, Bitmap> images = new HashMap<>();
        for (Cell cell : cells) {
            try {
                Bitmap backgroundImage = loadImage(cell);
                images.put(cell.getBackgroundImage(), Objects.requireNonNull(backgroundImage));
            } catch (Exception e) {
                errors = true;
                errorMessage = LOAD_SERVER_UNAVALABLE.getDescription();
                Log.e(TAG, "The request failed.", e);
                return images;
            }
        }
        return images;
    }

    private Bitmap loadImage(Cell cell) throws IOException{
        Response<ResponseBody> response =  habitatApi.getImage(cell.getBackgroundImage()).execute();
        if (response.isSuccessful()) {
            return BitmapFactory.decodeStream(Objects.requireNonNull(response.body()).byteStream());
        } else {
            errors = true;
            errorMessage = LOAD_IMAGE_REQUEST_FAILED.getDescription();
            Log.e(TAG, response.message());
            return null;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AreaLoader setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public boolean isErrors() {
        return errors;
    }

    private AreaLoader setErrors(boolean errors) {
        this.errors = errors;
        return this;
    }

}
