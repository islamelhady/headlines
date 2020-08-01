package com.elhady.headlines.Interface;

import com.elhady.headlines.Models.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("top-headlines")
    Call<WebSite> getSources(@Query("country") String country, @Query("apiKey") String api);

    @GET("everything")
    Call<WebSite> getNewsSearch(@Query("q") String keyword, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
}
