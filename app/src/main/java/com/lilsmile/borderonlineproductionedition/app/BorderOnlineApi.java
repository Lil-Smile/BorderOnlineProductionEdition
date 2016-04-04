package com.lilsmile.borderonlineproductionedition.app;




import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;


/**
 * Created by Smile on 28.02.16.
 */
public interface BorderOnlineApi {

    @GET("continents/")
    Call<JsonObject> getContinents();

    @GET("continents/{continent_id}/borders/")
    Call<JsonObject> getBorders(@Path("continent_id") int continentID);


    @GET("borders/{border_id}/info/")
    Call<JsonObject> getBorderInfo(@Path("border_id") int borderID);

    @GET("borders/{border_id}/checkpoints/{direction}/")
    Call<JsonObject> getCheckpoints(@Path("border_id") int borderID, @Path("direction") String direction);

    @GET("checkpoints/{checkpoint_id}/")
    Call<JsonObject> getCheckpointInfo(@Path("checkpoint_id") int checkpointID);
}
