package io.cell.androidclient;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.cell.androidclient.model.Address;
import io.cell.androidclient.model.Area;
import io.cell.androidclient.model.Cell;
import io.cell.androidclient.utils.tasks.LoadAreaTask;

public class MainActivity extends AppCompatActivity {

    private Area area = Area.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        loadArea();
        if (area.isLoaded()) {
            fillActivityArea();
        }
    }

    public void showInfo(View view) {
        Toast.makeText(getApplicationContext(),
                Integer.toString(area.getConvas().size()),
                Toast.LENGTH_SHORT).show();
    }

    private void loadArea() {
        try {
            LoadAreaTask loadAreaTask = new LoadAreaTask(this);
            loadAreaTask.execute();
            if (loadAreaTask.isErrors()) {
                Toast.makeText(this,
                        loadAreaTask.getErrorMessage(),
                        Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            Log.e("Habitat", e.getMessage(), e);
        }
    }

    private void fillActivityArea() {
        if (area.getConvas().isEmpty()) {
            return;
        }
        Integer maxX = area.getCurrentAddress().getX() + (area.getAreaSize() / 2);
        Integer minX = area.getCurrentAddress().getX() - (area.getAreaSize() / 2);
        Cell firstCell = area.getConvas().iterator().next();
        Integer offsetX = firstCell.getAddress().getX();
        Integer offsetY = firstCell.getAddress().getY() - 1;

        for (Cell cell : area.getConvas()) {
            Address address = cell.getAddress();
            if (address.getX() > maxX || address.getX() < minX) {
                continue;
            }
            String viewName = "cell_" + (address.getY() - offsetY) + "_" + (address.getX() - offsetX);
            Integer viewId = getResources().getIdentifier(viewName, "id", getPackageName());
            View cellView = findViewById(viewId);
            if (cellView == null) {
                continue;
            }
            Bitmap cellImage = area.getImageCache().get(cell.getBackgroundImage());
            cellView.setBackground(new BitmapDrawable(getResources(), cellImage));
        }
    }

    public Area getArea() {
        return area;
    }

    public MainActivity setArea(Area area) {
        this.area = area;
        return this;
    }
}
