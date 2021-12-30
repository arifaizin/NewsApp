package com.dicoding.newsapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.newsapp.data.NewsRepository;
import com.dicoding.newsapp.data.source.local.entity.NewsEntity;
import com.dicoding.newsapp.vo.Resource;

import java.util.List;


public class NewsViewModel extends ViewModel {
    private final NewsRepository newsRepository;

    public NewsViewModel(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public LiveData<Resource<List<NewsEntity>>> getHeadlineNews() {
        return newsRepository.getHeadlineNews();
    }

    public LiveData<List<NewsEntity>> getBookmarks() {
        return newsRepository.getBookmarkedCourses();
    }

    public void insertCourse(NewsEntity course) {
        newsRepository.setCourseBookmark(course, true);
    }

    public void deleteCourse(NewsEntity course) {
        newsRepository.setCourseBookmark(course, false);
    }

}


