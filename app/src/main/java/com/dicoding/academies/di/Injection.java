package com.dicoding.academies.di;

import android.content.Context;

import com.dicoding.academies.data.NewsRepository;
import com.dicoding.academies.data.source.local.room.NewsDao;
import com.dicoding.academies.data.source.local.room.NewsDatabase;
import com.dicoding.academies.data.source.remote.retrofit.ApiConfig;
import com.dicoding.academies.data.source.remote.retrofit.ApiService;
import com.dicoding.academies.utils.AppExecutors;

public class Injection {
    public static NewsRepository provideRepository(Context context) {
        ApiService apiService = ApiConfig.getApiService();
        NewsDatabase database = NewsDatabase.getInstance(context);
        NewsDao dao = database.newsDao();
        AppExecutors appExecutors = new AppExecutors();
        return NewsRepository.getInstance(apiService, dao, appExecutors);
    }
}
