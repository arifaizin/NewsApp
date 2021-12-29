package com.dicoding.academies.di;

import android.content.Context;

import com.dicoding.academies.data.AcademyRepository;
import com.dicoding.academies.data.source.local.LocalDataSource;
import com.dicoding.academies.data.source.local.room.AcademyDao;
import com.dicoding.academies.data.source.local.room.AcademyDatabase;
import com.dicoding.academies.data.source.remote.RemoteDataSource;
import com.dicoding.academies.data.source.remote.retrofit.ApiConfig;
import com.dicoding.academies.data.source.remote.retrofit.ApiService;
import com.dicoding.academies.utils.AppExecutors;
import com.dicoding.academies.utils.JsonHelper;

public class Injection {
    public static AcademyRepository provideRepository(Context context) {

        AcademyDatabase database = AcademyDatabase.getInstance(context);

        ApiService apiService = ApiConfig.getApiService();
        RemoteDataSource remoteDataSource = RemoteDataSource.getInstance(apiService);
        AcademyDao academyDao = database.academyDao();
        LocalDataSource localDataSource = LocalDataSource.getInstance(academyDao);
        AppExecutors appExecutors = new AppExecutors();

        return AcademyRepository.getInstance(apiService, academyDao, appExecutors);
    }
}
