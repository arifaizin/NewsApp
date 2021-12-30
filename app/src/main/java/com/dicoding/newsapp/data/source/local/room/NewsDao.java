package com.dicoding.newsapp.data.source.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dicoding.newsapp.data.source.local.entity.NewsEntity;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news ORDER BY publishedAt")
    LiveData<List<NewsEntity>> getCourses();

    @Query("SELECT * FROM news where bookmarked = 1")
    LiveData<List<NewsEntity>> getBookmarkedCourse();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCourses(List<NewsEntity> courses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(NewsEntity course);

    @Update
    void updateCourse(NewsEntity course);

    @Delete
    void deleteCourse(NewsEntity course);

    @Query("DELETE FROM news WHERE bookmarked = 0")
    void deleteAll();

    @Query("SELECT EXISTS(SELECT * FROM news WHERE title = :title)")
    Boolean isNewsSaved(String title);
}
