package com.peoplentech.devkh.blooddonor.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://10.16.20.41/BloodDonorDB/public/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
