package com.peoplentech.devkh.blooddonor.remote;
import com.peoplentech.devkh.blooddonor.model.ResObj;
import com.peoplentech.devkh.blooddonor.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface  UserService {


    @FormUrlEncoded
    @POST("register")
    Call<ResObj> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("birthdate") String birthDate,
            @Field("area") String address,
            @Field("phone") String number,
            @Field("status") String status,
            @Field("bloodgroup") String blood
    );

    @FormUrlEncoded
    @POST("login")
    Call<ResObj> getResponse(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("searchblood")
    Call<ResObj> getDonor(
            @Field("bloodgroup") String blood
    );

    @GET("users")
    Call<Users> getUsers();

}