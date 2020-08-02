package com.elhady.headlines;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elhady.headlines.Adapter.ListSourceAdapter;
import com.elhady.headlines.Common.Common;
import com.elhady.headlines.Interface.NewsService;
import com.elhady.headlines.Models.Article;
import com.elhady.headlines.Models.WebSite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView listWebsite;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsService newsService;
    private ListSourceAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorRelativeLayout;
    private ImageView errorImage;
    private TextView errorTitle,errorMessage;
    private Button errorBtnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        listWebsite = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        listWebsite.setLayoutManager(layoutManager);
        listWebsite.setItemAnimator(new DefaultItemAnimator());
        listWebsite.setNestedScrollingEnabled(false);

        //loadWebSiteSource();
        onLoadingSwipeRefresh("");

        errorRelativeLayout = findViewById(R.id.error_layout);
        errorImage = findViewById(R.id.error_image);
        errorTitle = findViewById(R.id.error_title);
        errorMessage = findViewById(R.id.error_message);
        errorBtnRetry= findViewById(R.id.btnRetry);
    }

    private void loadWebSiteSource(String keyword) {

        errorRelativeLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        newsService = Common.getNewsService();
        Call<WebSite> call;

        if (keyword.length() > 0) {
            call = newsService.getNewsSearch(keyword, "publishedAt", Common.API);
        } else {
            call = newsService.getSources("eg", Common.API);
        }
        call.enqueue(new Callback<WebSite>() {
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

                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    swipeRefreshLayout.setRefreshing(false);

                    showErrorMessage(R.drawable.signal, "No Result", "Please Try Again!\n");
                }
            }

            @Override
            public void onFailure(Call<WebSite> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.signal, "Oops...", "Network failure Please Try Again!\n");

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
        searchView.setQueryHint("Search news...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    onLoadingSwipeRefresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //loadWebSiteSource(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
        return true;


    }

    @Override
    public void onRefresh() {
        loadWebSiteSource("");
    }
    private void onLoadingSwipeRefresh(final String keyword) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadWebSiteSource(keyword);
            }
        });
    }

    private void showErrorMessage (int imageView , String title, String message){

        if (errorRelativeLayout.getVisibility() == View.GONE){
            errorRelativeLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        errorBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onLoadingSwipeRefresh("");
            }
        });
    }
}
