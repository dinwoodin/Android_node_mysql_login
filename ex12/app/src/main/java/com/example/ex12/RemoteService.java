package com.example.ex12;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteService {
    //node mysql과 연결하는것
    public static final String BASE_URL="http://192.168.1.90:3000";


    @GET("/users/list.json")
    Call<List<UserVO>> list();


}
