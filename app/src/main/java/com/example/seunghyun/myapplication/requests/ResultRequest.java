package com.example.seunghyun.myapplication.requests;

import com.example.seunghyun.myapplication.responses.ResultResponse;
import com.example.seunghyun.myapplication.responses.SuccessResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ji Hoon on 2018-09-02.
 */

public interface ResultRequest {
    @GET("/upload")
    Call<ResultResponse> requestResult(@Query("option") String option, @Query("id")String id);
}
