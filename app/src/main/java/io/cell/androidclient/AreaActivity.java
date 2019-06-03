package io.cell.androidclient;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import io.cell.androidclient.custom.views.CellView;
import io.cell.androidclient.model.Address;
import io.cell.androidclient.model.Area;
import io.cell.androidclient.model.Cell;
import io.cell.androidclient.utils.cache.ImageCache;
import io.cell.androidclient.utils.cache.ImageCacheSingleton;
import io.cell.androidclient.utils.tasks.AreaLoader;
import io.cell.androidclient.utils.tasks.AreaLoader_;
import io.cell.androidclient.utils.typeAdapters.TreeMapTypeAdapterFactory;

@EActivity(R.layout.area_activity)
public class AreaActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Area> {

    private static final int AREA_LOADER_ID = 1;

    private final String TAG = getClass().getSimpleName();

    @Bean
    AreaLoader areaLoader;
    @ViewById(R.id.mainTable)
    TableLayout mainTable;
    @Bean(ImageCacheSingleton.class)
    ImageCache imageCache;

    private Area area;
    private ProgressDialog crossIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @AfterViews
    public void init() {
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(new TreeMapTypeAdapterFactory())
                .registerTypeAdapter(Map.class, TreeMapTypeAdapterFactory.newCreator())
                .create();

        String areaJson = getIntent().getExtras().getString(Area.class.getCanonicalName());
        area = gson.fromJson(areaJson, Area.class);
        registerContextMenu();
        if (area.isLoaded()) {
            fillActivityArea();
        }
        areaLoader = (AreaLoader) LoaderManager.getInstance(this).initLoader(AREA_LOADER_ID, new Bundle(), this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        prepareCellContextMenu(menu, view);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cell_view_menu, menu);

        MenuItem infoItem = menu.findItem(R.id.infoItem);
        infoItem.setActionView(view);

        MenuItem crossItem = menu.findItem(R.id.crossItem);
        boolean crossItemVisible = area.getCanvas().get(
                ((CellView) view).getAddress())
                .isMovable();
        crossItem.setActionView(view);
        crossItem.setVisible(crossItemVisible);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crossItem:
                onCrossItemSelected(item);
                break;
            case R.id.infoItem:
                onInfoItemSelected(item);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public void onCrossItemSelected(MenuItem item) {
        CellView currentView = (CellView) item.getActionView();
//        Integer totalTime = area.getCanvas()
//                .get(currentView.getAddress())
//                .getMovementRate();
        int totalTime = 10; // Временно
        crossIndicator = new ProgressDialog(this);
        prepareCrossIndicatorView(item);
        crossIndicator.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        crossIndicator.setProgressNumberFormat(getResources().getString(R.string.process_number_format));
        crossIndicator.setProgressPercentFormat(null);
        crossIndicator.setMax(totalTime);
        crossIndicator.setCancelable(false);
        crossIndicator.show();

        startCross(currentView.getAddress());
        startCrossProgress(totalTime);
    }

    private void prepareCrossIndicatorView(MenuItem item) {
        CellView currentView = (CellView) item.getActionView();
        String crossMessageTemplate = getResources().getString(R.string.crossMessageTemplate);
        String crossMessage = String.format(crossMessageTemplate,
                area.getCurrentAddress().getX(), area.getCurrentAddress().getY(),
                currentView.getAddress().getX(), currentView.getAddress().getY());
        String title = getResources().getString(R.string.crossCaption);
        View customTitle = getLayoutInflater().inflate(R.layout.cross_progress_bar_title, null);
        ((TextView) customTitle.findViewById(R.id.crossProgressLeftTitle)).setText(title);
        ((TextView) customTitle.findViewById(R.id.crossProgressRightTitle)).setText(crossMessage);
        crossIndicator.setCustomTitle(customTitle);
    }

    public void onInfoItemSelected(MenuItem item) {
        Toast.makeText(this,
                ((CellView) item.getActionView()).getAddress().toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Background
    public void startCrossProgress(final Integer time) {
        int counter = time;
        crossIndicator.setProgress(counter);
        while (counter > 0) {
            try {
                Thread.sleep(1000);
                counter--;
                crossIndicator.setProgress(counter);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        updateActivityAreaStage();
    }

    @UiThread
    public void updateActivityAreaStage() {
        if (!area.getCurrentAddress().equals(areaLoader.getArea().getCurrentAddress())
                && areaLoader.getArea().isLoaded()) {
            area = areaLoader.getArea();
            fillActivityArea();
        }
        crossIndicator.dismiss();
    }

    private void startCross(Address targetAddress) {
        areaLoader.setTargetAddress(targetAddress);
        LoaderManager.getInstance(this).restartLoader(AREA_LOADER_ID, new Bundle(), this);
    }

    public void showMenu(View view) {
        openContextMenu(view);
    }

    private void registerContextMenu() {
        for (int i = 0; i < mainTable.getChildCount(); i++) {
            View rowView = mainTable.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow tableRow = (TableRow) rowView;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View frameView = tableRow.getChildAt(j);
                    FrameLayout frameLayout = (FrameLayout) frameView;
                    registerForContextMenu(frameLayout.getChildAt(0));
                }
            }
        }
    }

    private void prepareCellContextMenu(ContextMenu menu, View view) {
        View cellContextMenuHeader = getLayoutInflater().inflate(R.layout.cell_context_menu_header, null, false);
        TextView cellContextTitle = cellContextMenuHeader.findViewById(R.id.cellContextTitle);
        cellContextTitle.setText(getMenuTitle(view));
        TextView cellContextAddressTitle = cellContextMenuHeader.findViewById(R.id.cellContextAddressTitle);
        cellContextAddressTitle.setText(getAddressMenuTitle(view));
        menu.setHeaderView(cellContextMenuHeader);
    }

    private String getMenuTitle(View view) {
        CellView cellView = (CellView) view;
        Address address = cellView.getAddress();
        return getResources().getString(R.string.regionCaption)
                + ": "
                + address.getRegionIndex();
    }

    private String getAddressMenuTitle(View view) {
        CellView cellView = (CellView) view;
        Address address = cellView.getAddress();
        StringBuilder titleBuilder = new StringBuilder(getResources().getString(R.string.cellCaption));
        titleBuilder.append(": ")
                .append(address.getX()).append(".")
                .append(address.getY());
        return titleBuilder.toString();
    }

    private void fillActivityArea() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fillVerticalActivityArea();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fillHorizontalActivityArea();
        }
    }

    private void fillVerticalActivityArea() {
        if (area.getCanvas().isEmpty()) {
            return;
        }
        Integer maxX = area.getCurrentAddress().getX() + (area.getAreaSize() / 2);
        Integer minX = area.getCurrentAddress().getX() - (area.getAreaSize() / 2);
        TreeSet<Cell> cells = new TreeSet<>(area.getCanvas().values());
        Cell firstCell = cells.first();
        Integer offsetX = firstCell.getAddress().getX();
        Integer offsetY = firstCell.getAddress().getY() - 1;

        for (Cell cell : cells) {
            Address address = cell.getAddress();
            if (address.getX() > maxX || address.getX() < minX) {
                continue;
            }
            String viewName = "cell_v_" + (address.getY() - offsetY) + "_" + (address.getX() - offsetX);
            Integer viewId = getResources().getIdentifier(viewName, "id", getPackageName());
            CellView cellView = findViewById(viewId);
            if (cellView == null) {
                continue;
            }
            Bitmap cellImage = imageCache.getImage(cell.getBackgroundImage());
            cellView.setBackground(new BitmapDrawable(getResources(), cellImage));
            cellView.setAddress(cell.getAddress());
        }
    }

    private void fillHorizontalActivityArea() {
        if (area.getCanvas().isEmpty()) {
            return;
        }
        Integer maxY = area.getCurrentAddress().getY() + (area.getAreaSize() / 2);
        Integer minY = area.getCurrentAddress().getY() - (area.getAreaSize() / 2);
        Cell firstCell = (Cell) ((TreeMap) area.getCanvas()).firstEntry().getValue();
        Integer offsetX = firstCell.getAddress().getX() - 1;
        Integer offsetY = firstCell.getAddress().getY();

        for (Cell cell : area.getCanvas().values()) {
            Address address = cell.getAddress();
            if (address.getY() > maxY || address.getY() < minY) {
                continue;
            }
            String viewName = "cell_h_" + (address.getY() - offsetY) + "_" + (address.getX() - offsetX);
            Integer viewId = getResources().getIdentifier(viewName, "id", getPackageName());
            CellView cellView = findViewById(viewId);
            if (cellView == null) {
                continue;
            }
            Bitmap cellImage = imageCache.getImage(cell.getBackgroundImage());
            cellView.setBackground(new BitmapDrawable(getResources(), cellImage));
            cellView.setAddress(cell.getAddress());
        }
    }

    public Area getArea() {
        return area;
    }

    public AreaActivity setArea(Area area) {
        this.area = area;
        return this;
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
    public void onLoadFinished(@NonNull Loader<Area> loader, Area targetArea) {
        if (loader.getId() == AREA_LOADER_ID) {
            AreaLoader areaLoader = (AreaLoader) loader;
            if (areaLoader.isErrors()) {
                Toast.makeText(this,
                        areaLoader.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
                crossIndicator.dismiss();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Area> loader) {
    }
}
