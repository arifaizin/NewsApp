package com.dicoding.academies.ui.academy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.academies.data.AcademyRepository;
import com.dicoding.academies.data.source.local.entity.NewsEntity;
import com.dicoding.academies.vo.Resource;

import java.util.List;


public class AcademyViewModel extends ViewModel {
    private AcademyRepository academyRepository;

    public AcademyViewModel(AcademyRepository mAcademyRepository) {
        this.academyRepository = mAcademyRepository;
    }

    public LiveData<Resource<List<NewsEntity>>> getHeadlineNews() {
        LiveData<Resource<List<NewsEntity>>> news = academyRepository.getHeadlineNews();
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

    public void insertCourse(NewsEntity course) {
        academyRepository.setCourseBookmark(course, true);
    }

    public void deleteCourse(NewsEntity course) {
        academyRepository.setCourseBookmark(course, false);
    }

}


