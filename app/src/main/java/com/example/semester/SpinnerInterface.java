package com.example.semester;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpinnerInterface {

    String JSONURL = "http://192.168.2.7/crud/tampil_semester.php";

    @GET("tampil_semester.php")
    Call<String> getJSONString();
}
