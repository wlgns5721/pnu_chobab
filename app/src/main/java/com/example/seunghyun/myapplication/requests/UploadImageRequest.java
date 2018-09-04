package com.example.seunghyun.myapplication.requests;


import com.example.seunghyun.myapplication.responses.SuccessResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Ji Hoon on 2018-06-23.
 */

public interface UploadImageRequest {
    @Multipart
    @POST("/upload/{productid}")
    Call<SuccessResponse> uploadImage(@Part MultipartBody.Part image, @Path("productid") String productId);
}
