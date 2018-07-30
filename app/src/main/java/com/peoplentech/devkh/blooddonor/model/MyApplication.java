package com.peoplentech.devkh.blooddonor.model;

import android.app.Application;



import com.google.gson.ExclusionStrategy;

import com.google.gson.FieldAttributes;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;

import com.peoplentech.devkh.blooddonor.networking.NearByApi;
import com.peoplentech.devkh.blooddonor.remote.Constants;


import java.util.Collection;

import java.util.concurrent.TimeUnit;



import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {



    NearByApi nearByApi = null;

    static MyApplication app;



    @Override

    public void onCreate() {

        super.onCreate();

        app = this;

    }





    public NearByApi getApiService() {

        if (nearByApi == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).readTimeout(80, TimeUnit.SECONDS).connectTimeout(80, TimeUnit.SECONDS).addInterceptor(interceptor).build();



            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.PLACE_API_BASE_URL).addConverterFactory(getApiConvertorFactory()).client(client).build();



            nearByApi = retrofit.create(NearByApi.class);

            return nearByApi;

        } else {

            return nearByApi;

        }

    }



    private static GsonConverterFactory getApiConvertorFactory() {

        return GsonConverterFactory.create();

    }





    public static MyApplication getApp() {

        return app;

    }



}
