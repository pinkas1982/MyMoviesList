package com.hanselandpetal.catalog;

import com.hanselandpetal.catalog.model.Flower;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by pinkas on 7/14/2016.
 */
public interface FlowersApi {

    @GET("/feeds/flowers.json")
    public void getFeed(Callback<List<Flower>> response);


}
