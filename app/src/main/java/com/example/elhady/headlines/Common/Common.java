package com.example.elhady.headlines.Common;

import com.example.elhady.headlines.Interface.NewsService;
import com.example.elhady.headlines.Remote.RetrofitClient;

public class Common {

    private static final String BASE_URL = "http://newsapi.org/v2/";

    public static final String API = "e4befc80710444afa7f93f67a5790d57";

    public static NewsService getNewsService (){

        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }
}
