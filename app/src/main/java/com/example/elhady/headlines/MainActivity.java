package com.example.elhady.headlines;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.elhady.headlines.Adapter.ListSourceAdapter;
import com.example.elhady.headlines.Common.Common;
import com.example.elhady.headlines.Interface.NewsService;
import com.example.elhady.headlines.model.Article;
import com.example.elhady.headlines.model.WebSite;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listWebsite;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsService newsService;
    private ListSourceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listWebsite = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        listWebsite.setLayoutManager(layoutManager);
        listWebsite.setItemAnimator(new DefaultItemAnimator());
        listWebsite.setNestedScrollingEnabled(false);


        loadWebSiteSource();

    }

    public void loadWebSiteSource() {
        newsService = Common.getNewsService();
        newsService.getSources("eg", Common.API).enqueue(new Callback<WebSite>() {
            @Override
            public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticles();
                    adapter = new ListSourceAdapter(MainActivity.this, articles);
                    listWebsite.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<WebSite> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No result", Toast.LENGTH_SHORT).show();


            }
        });
    }


}
