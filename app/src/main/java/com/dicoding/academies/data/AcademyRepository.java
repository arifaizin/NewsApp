package com.dicoding.academies.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.dicoding.academies.BuildConfig;
import com.dicoding.academies.data.source.local.entity.CourseEntity;
import com.dicoding.academies.data.source.local.entity.CourseWithModule;
import com.dicoding.academies.data.source.local.entity.ModuleEntity;
import com.dicoding.academies.data.source.local.room.AcademyDao;
import com.dicoding.academies.data.source.remote.response.ArticlesItem;
import com.dicoding.academies.data.source.remote.response.NewsResponse;
import com.dicoding.academies.data.source.remote.retrofit.ApiService;
import com.dicoding.academies.utils.AppExecutors;
import com.dicoding.academies.utils.EspressoIdlingResource;
import com.dicoding.academies.vo.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademyRepository implements AcademyDataSource {

    private volatile static AcademyRepository INSTANCE = null;

    private final ApiService apiService;
    private final AcademyDao newsDao;
    private final AppExecutors appExecutors;

    private MediatorLiveData<Resource<List<CourseEntity>>> result = new MediatorLiveData<>();

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

    @Override
    public LiveData<Resource<List<CourseEntity>>> getHeadlineNews() {
        result.setValue(Resource.loading(null));

        Call<NewsResponse> client = apiService.getNews(BuildConfig.API_KEY);
        client.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    List<ArticlesItem> articles = response.body().getArticles();

                    ArrayList<CourseEntity> courseList = new ArrayList<>();
                    for (ArticlesItem article : articles) {
                        CourseEntity course = new CourseEntity(
                                article.getTitle(),
                                article.getTitle(),
                                article.getDescription(),
                                article.getPublishedAt(),
                                false,
                                article.getUrlToImage());

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

        LiveData<List<CourseEntity>> localData = newsDao.getCourses();
        result.addSource(localData, newData -> {
                    result.setValue(Resource.success(newData));
                }
        );

        return result;
    }

    @Override
    public LiveData<Resource<CourseWithModule>> getCourseWithModules(String courseId) {
        return null;
    }

    @Override
    public LiveData<Resource<List<ModuleEntity>>> getAllModulesByCourse(String courseId) {
        return null;
    }

    @Override
    public LiveData<Resource<ModuleEntity>> getContent(String moduleId) {
        return null;
    }

    @Override
    public LiveData<List<CourseEntity>> getBookmarkedCourses() {
        return newsDao.getBookmarkedCourse();
    }

    public void insertCourse(CourseEntity course) {
        appExecutors.diskIO().execute(() -> {
            newsDao.insertCourse(course);
        });
    }

    public void deleteCourse(CourseEntity course) {
        appExecutors.diskIO().execute(() -> {
            newsDao.deleteCourse(course);
        });
    }

    public Boolean isNewsSaved(String title) {
        return newsDao.isNewsSaved(title);
    }

    @Override
    public void setCourseBookmark(CourseEntity course, boolean state) {
        appExecutors.diskIO().execute(() -> {
            if (state) {
                newsDao.updateCourse(course);
            } else {
                newsDao.updateCourse(course);
            }
        });
    }

    @Override
    public void setReadModule(ModuleEntity module) {

    }
}

