package com.example.seunghyun.myapplication.requests;

import com.example.seunghyun.myapplication.responses.SuccessResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ji Hoon on 2018-09-04.
 */

public interface FunctionRequest {
    @POST("/")
    Call<SuccessResponse> recognition(@Query("word") String keyword);

    @POST("/")
    Call<SuccessResponse> expiration();
}
