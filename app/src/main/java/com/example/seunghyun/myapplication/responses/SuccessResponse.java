package com.example.seunghyun.myapplication.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ji Hoon on 2018-06-24.
 */

public class SuccessResponse {
    @SerializedName("isSuccess")
    @Expose
    private String isSuccess = null;

    @SerializedName("id")
    @Expose
    private Integer id = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getIsSuccess() {

        return isSuccess;
    }
}
