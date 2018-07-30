package com.peoplentech.devkh.blooddonor.networking;

import com.peoplentech.devkh.blooddonor.maps.NearByApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearByApi {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyADCVwbHARqPo32xCC5shdGAWf15xxPzzE")
    Call<NearByApiResponse> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyADCVwbHARqPo32xCC5shdGAWf15xxPzzE&keyword=bloodbank")
    Call<NearByApiResponse> getBloodBank( @Query("location") String location, @Query("radius") int radius);

}
