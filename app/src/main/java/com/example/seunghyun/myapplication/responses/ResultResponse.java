package com.example.seunghyun.myapplication.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ji Hoon on 2018-09-04.
 */

public class ResultResponse {
    @SerializedName("iscomplete")
    @Expose
    private String isComplete;

    @SerializedName("product")
    @Expose
    private String product = null;

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getIsComplete() {

        return isComplete;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

}
