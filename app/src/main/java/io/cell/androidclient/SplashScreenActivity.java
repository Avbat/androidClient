package io.cell.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.cell.androidclient.model.Area;
import io.cell.androidclient.utils.tasks.AreaLoader;
import io.cell.androidclient.utils.tasks.AreaLoader_;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Активити для первоначальной загрузки текущей для пользователя области.
 */
@EActivity(R.layout.splash_screen)
public class SplashScreenActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Area> {

    @Bean
    AreaLoader areaLoader;
    @ViewById(R.id.loadCaptionFrame)
    FrameLayout loadCaption;
    @ViewById(R.id.connectButton)
    Button connectButton;

    private static final int AREA_LOADER_ID = 1;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .create();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @AfterViews
    public void init() {
        connectButton.setVisibility(INVISIBLE);
        loadCaption.setVisibility(VISIBLE);
        areaLoader = (AreaLoader) LoaderManager.getInstance(this).initLoader(AREA_LOADER_ID, new Bundle(), this);
    }

    public void connectOnClick(View view) {
        loadCaption.setVisibility(VISIBLE);
        connectButton.setVisibility(INVISIBLE);
        areaLoader.onContentChanged();
    }

    @NonNull
    @Override
    public Loader<Area> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == AREA_LOADER_ID && this.areaLoader != null) {
            return this.areaLoader;
        }
        return AreaLoader_.getInstance_(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Area> loader, Area area) {
        if (loader.getId() == AREA_LOADER_ID) {
            AreaLoader areaLoader = (AreaLoader) loader;
            if (areaLoader.isErrors()) {
                loadCaption.setVisibility(INVISIBLE);
                connectButton.setVisibility(VISIBLE);
                Toast.makeText(this,
                        areaLoader.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Intent mainIntent = new Intent(this, AreaActivity_.class);
            mainIntent.putExtra(Area.class.getCanonicalName(), gson.toJson(area));
            this.startActivity(mainIntent);
            this.finish();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Area> loader) {
    }
}
