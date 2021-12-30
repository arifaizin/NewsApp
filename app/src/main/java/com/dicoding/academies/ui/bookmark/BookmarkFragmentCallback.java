package com.dicoding.academies.ui.bookmark;


import com.dicoding.academies.data.source.local.entity.NewsEntity;

interface BookmarkFragmentCallback {
    void onShareClick(NewsEntity course);
}

