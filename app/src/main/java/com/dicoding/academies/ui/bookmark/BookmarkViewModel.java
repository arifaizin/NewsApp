package com.dicoding.academies.ui.bookmark;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.academies.data.AcademyRepository;
import com.dicoding.academies.data.source.local.entity.NewsEntity;

import java.util.List;


public class BookmarkViewModel extends ViewModel {
    private AcademyRepository academyRepository;

    public BookmarkViewModel(AcademyRepository mAcademyRepository) {
        this.academyRepository = mAcademyRepository;
    }

    public LiveData<List<NewsEntity>> getBookmarks() {
        return academyRepository.getBookmarkedCourses();
    }
}

