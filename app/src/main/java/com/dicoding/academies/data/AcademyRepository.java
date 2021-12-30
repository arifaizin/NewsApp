package com.dicoding.academies.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.dicoding.academies.BuildConfig;
import com.dicoding.academies.data.source.local.entity.NewsEntity;
import com.dicoding.academies.data.source.local.room.AcademyDao;
import com.dicoding.academies.data.source.remote.response.ArticlesItem;
import com.dicoding.academies.data.source.remote.response.NewsResponse;
import com.dicoding.academies.data.source.remote.retrofit.ApiService;
import com.dicoding.academies.utils.AppExecutors;
import com.dicoding.academies.vo.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademyRepository {

    private volatile static AcademyRepository INSTANCE = null;

    private final ApiService apiService;
    private final AcademyDao newsDao;
    private final AppExecutors appExecutors;

    private MediatorLiveData<Resource<List<NewsEntity>>> result = new MediatorLiveData<>();

    private AcademyRepository(@NonNull ApiService apiService, @NonNull AcademyDao newsDao, AppExecutors appExecutors) {
        this.apiService = apiService;
        this.newsDao = newsDao;
        this.appExecutors = appExecutors;
    }

    public static AcademyRepository getInstance(ApiService apiService, AcademyDao newsDao, AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (AcademyRepository.class) {
                INSTANCE = new AcademyRepository(apiService, newsDao, appExecutors);
            }
        }
        return INSTANCE;
    }

    public LiveData<Resource<List<NewsEntity>>> getHeadlineNews() {
        result.setValue(Resource.loading(null));

        Call<NewsResponse> client = apiService.getNews(BuildConfig.API_KEY);
        client.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    List<ArticlesItem> articles = response.body().getArticles();

                    ArrayList<NewsEntity> courseList = new ArrayList<>();
                    for (ArticlesItem article : articles) {
                        NewsEntity course = new NewsEntity(
                                article.getTitle(),
                                article.getPublishedAt(),
                                article.getUrlToImage(),
                                false
                                );

                        courseList.add(course);
                    }
                    appExecutors.diskIO().execute(() -> {
                        newsDao.deleteAll();
                        newsDao.insertCourses(courseList);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                result.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });

        LiveData<List<NewsEntity>> localData = newsDao.getCourses();
        result.addSource(localData, newData -> {
                    result.setValue(Resource.success(newData));
                }
        );

        return result;
    }

    public LiveData<List<NewsEntity>> getBookmarkedCourses() {
        return newsDao.getBookmarkedCourse();
    }

    public void insertCourse(NewsEntity course) {
        appExecutors.diskIO().execute(() -> newsDao.insertCourse(course));
    }

    public void deleteCourse(NewsEntity course) {
        appExecutors.diskIO().execute(() -> newsDao.deleteCourse(course));
    }

    public Boolean isNewsSaved(String title) {
        return newsDao.isNewsSaved(title);
    }

    public void setCourseBookmark(NewsEntity course, boolean state) {
        appExecutors.diskIO().execute(() -> {
            newsDao.updateCourse(course);
        });
    }

}

