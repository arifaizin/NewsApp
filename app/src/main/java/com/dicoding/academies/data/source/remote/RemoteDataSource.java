package com.dicoding.academies.data.source.remote;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dicoding.academies.BuildConfig;
import com.dicoding.academies.data.source.remote.response.ArticlesItem;
import com.dicoding.academies.data.source.remote.response.ContentResponse;
import com.dicoding.academies.data.source.remote.response.NewsResponse;
import com.dicoding.academies.data.source.remote.response.ModuleResponse;
import com.dicoding.academies.data.source.remote.response.NewsResponse;
import com.dicoding.academies.data.source.remote.retrofit.ApiService;
import com.dicoding.academies.utils.EspressoIdlingResource;
import com.dicoding.academies.utils.JsonHelper;
import com.dicoding.academies.vo.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource {

    private static RemoteDataSource INSTANCE;
    private ApiService apiService;

    private RemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    public static RemoteDataSource getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(apiService);
        }
        return INSTANCE;
    }

    public LiveData<Resource<List<ArticlesItem>>> getHeadlineNews() {
//        EspressoIdlingResource.increment();
        MutableLiveData<Resource<List<ArticlesItem>>> resultCourse = new MutableLiveData<>();
        Call<NewsResponse> client = apiService.getNews(BuildConfig.API_KEY);
        client.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful()){
                    resultCourse.setValue(Resource.success(response.body().getArticles()));
//                    EspressoIdlingResource.decrement();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                resultCourse.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return resultCourse;
    }
}

