package io.cell.androidclient.api.habitat;

import java.util.Set;

import io.cell.androidclient.model.Cell;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit REST Api implementation
 */
public interface HabitatApi {

    @GET("/area")
    Call<Set<Cell>> getArea(@Query("x") Integer x,
                           @Query("y") Integer y,
                           @Query("areaSize") Integer areaSize);

    @GET("/image/{filename}")
    Call<ResponseBody> getImage(@Path("filename") String filename);
}
