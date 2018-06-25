package com.example.shubhang.ecommerce.network;

import com.example.shubhang.ecommerce.model.ProductList;
import com.example.shubhang.ecommerce.model.products;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface
GetDataService {

@GET("productFilter")
        Call<ProductList> getProducts(@Query("token") String token, @Query("format") String format, @Query("q") String q);
}
        //