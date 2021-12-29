package com.dicoding.academies.data.source.remote.retrofit;

import com.dicoding.academies.data.source.remote.response.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("top-headlines?country=id&category=science")
    Call<NewsResponse> getNews(@Query("apiKey") String apiKey);
}
