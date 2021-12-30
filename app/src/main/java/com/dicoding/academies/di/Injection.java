package com.dicoding.academies.di;

import android.content.Context;

import com.dicoding.academies.data.AcademyRepository;
import com.dicoding.academies.data.source.local.room.AcademyDao;
import com.dicoding.academies.data.source.local.room.AcademyDatabase;
import com.dicoding.academies.data.source.remote.retrofit.ApiConfig;
import com.dicoding.academies.data.source.remote.retrofit.ApiService;
import com.dicoding.academies.utils.AppExecutors;

public class Injection {
    public static AcademyRepository provideRepository(Context context) {

        AcademyDatabase database = AcademyDatabase.getInstance(context);

        ApiService apiService = ApiConfig.getApiService();
        AcademyDao academyDao = database.academyDao();
        AppExecutors appExecutors = new AppExecutors();

        return AcademyRepository.getInstance(apiService, academyDao, appExecutors);
    }
}
