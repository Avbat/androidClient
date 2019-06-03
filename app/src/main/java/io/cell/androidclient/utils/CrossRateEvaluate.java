package io.cell.androidclient.utils;

import android.content.Context;
import android.content.res.Resources;

import org.androidannotations.annotations.EBean;

import io.cell.androidclient.R;
import io.cell.androidclient.model.Cell;

@EBean
public class CrossRateEvaluate {

    private Context context;

    public CrossRateEvaluate(Context context) {
        this.context = context;
    }

    public Integer evaluateMovementRate(Cell cell) {
        Integer rate = context.getResources().getInteger(R.integer.movementRate);
        Float cellRate = cell.getMovementRate() == null ? 0 : cell.getMovementRate();
        return Float.valueOf(rate * cellRate).intValue();
    }

    public Integer evaluateFlightRate(Cell cell) {
        Integer rate = Resources.getSystem().getInteger(R.integer.flightRate);
        Float cellRate = cell.getFlightRate() == null ? 0 : cell.getFlightRate();
        return Float.valueOf(rate * cellRate).intValue();
    }
}
