package com.dicoding.academies.ui.academy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.dicoding.academies.data.AcademyRepository;
import com.dicoding.academies.data.source.local.entity.CourseEntity;
import com.dicoding.academies.data.source.remote.response.ArticlesItem;
import com.dicoding.academies.vo.Resource;

import java.util.ArrayList;
import java.util.List;


public class AcademyViewModel extends ViewModel {
    private AcademyRepository academyRepository;

    public AcademyViewModel(AcademyRepository mAcademyRepository) {
        this.academyRepository = mAcademyRepository;
    }

    public LiveData<Resource<List<CourseEntity>>> getHeadlineNews() {
        LiveData<Resource<List<CourseEntity>>> news = academyRepository.getHeadlineNews();
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

    public void insertCourse(CourseEntity course) {
        academyRepository.setCourseBookmark(course, true);
    }

    public void deleteCourse(CourseEntity course) {
        academyRepository.setCourseBookmark(course, false);
    }

}


