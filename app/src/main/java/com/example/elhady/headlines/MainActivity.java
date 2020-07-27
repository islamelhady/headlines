package com.example.elhady.headlines;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
         (R.layout.activity_main);


        listWebsite = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        listWebsite.setLayoutManager(layoutManager);
        listWebsite.setItemAnimator(new DefaultItemAnimator());
        listWebsite.setNestedScrollingEnabled(false);


        loadWebSiteSource("");

    }

    public void loadWebSiteSource(String keyword) {
        newsService = Common.getNewsService();

        if (keyword.length() > 0) {
            newsService.getNewsSearch(keyword, "ar", "publishedAt", Common.API);
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2)
                    loadWebSiteSource(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadWebSiteSource(newText);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);


        return true;
    }
}
