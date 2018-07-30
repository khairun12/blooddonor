package com.peoplentech.devkh.blooddonor.remote;

import com.peoplentech.devkh.blooddonor.maps.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 7/23/2018.
 */

public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyD0FJzAC1zdpZAnvdFUKru5KZSyOW2GnTY")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    //Test
    @GET("$type/$location/$radius")
    Call<Example> getNewPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
