package com.flowz.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.Toast;

import com.flowz.newsapp.models.Article;
import com.flowz.newsapp.models.News;
import com.flowz.newsapp.models.api.ApiClient;
import com.flowz.newsapp.models.api.ApiInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "6496c837bca04576875ffbe8115eb2f5";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        loadJson();
    }

    public void loadJson(){

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();

        Call<News> call;
        call = apiInterface.getNews(country, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
               if (response.isSuccessful() && response.body().getArticle() != null) {

                   if (!articles.isEmpty()){
                      articles.clear();
                   }


                   articles = response.body().getArticle();
                   adapter = new Adapter(articles, MainActivity.this);
                   recyclerView.setAdapter(adapter);
                   adapter.notifyDataSetChanged();


               }else {
                   Toast.makeText(MainActivity.this, "No Result", Toast.LENGTH_SHORT).show();

               }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }


}
