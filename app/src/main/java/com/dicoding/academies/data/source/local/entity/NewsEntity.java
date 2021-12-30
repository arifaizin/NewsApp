package com.dicoding.academies.data.source.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class NewsEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "publishedAt")
    private String publishedAt;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    @ColumnInfo(name = "bookmarked")
    private boolean bookmarked;

    public NewsEntity(@NonNull String title, String publishedAt, String imagePath, Boolean bookmarked) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.imagePath = imagePath;
        this.bookmarked = bookmarked;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
