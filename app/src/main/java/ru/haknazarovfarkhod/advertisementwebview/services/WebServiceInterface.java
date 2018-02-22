package ru.haknazarovfarkhod.advertisementwebview.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.haknazarovfarkhod.advertisementwebview.dao.PostRequest;

/**
 * Created by USER on 21.02.2018.
 */

public interface WebServiceInterface {

    @GET("adviator/index.php")
    Call<PostRequest> getData(@Query("id") String imsi);

}
