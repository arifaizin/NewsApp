package com.dicoding.academies.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.academies.data.NewsRepository;
import com.dicoding.academies.data.source.local.entity.NewsEntity;
import com.dicoding.academies.vo.Resource;

import java.util.List;


public class NewsViewModel extends ViewModel {
    private NewsRepository newsRepository;

    public NewsViewModel(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public LiveData<Resource<List<NewsEntity>>> getHeadlineNews() {
        LiveData<Resource<List<NewsEntity>>> news = newsRepository.getHeadlineNews();
//        LiveData<Resource<List<ArticlesItem>>> newNews = Transformations.map(news, data -> {
//            List<ArticlesItem> newItem = new ArrayList();
//            for (ArticlesItem item : data.data) {
//                Boolean saved = academyRepository.isNewsSaved(item.getTitle());
//                item.setSaved(saved);
//                newItem.add(item);
//            }
//            return newItem;
//        });
        return news;
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


