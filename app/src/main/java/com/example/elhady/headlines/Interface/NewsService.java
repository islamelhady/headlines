package com.example.elhady.headlines.Interface;


import com.example.elhady.headlines.model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Call<WebSite> getSources(@Query("country") String country, @Query("apiKey") String api);

    @GET("everything")
    Call<WebSite> getNewsSearch(@Query("q") String keyword, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
}
