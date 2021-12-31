package com.dicoding.newsapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dicoding.newsapp.data.Result;
import com.dicoding.newsapp.data.source.local.entity.NewsEntity;
import com.dicoding.newsapp.databinding.FragmentNewsBinding;

import java.util.List;

public class NewsFragment extends Fragment {

    public static final String ARG_TAB = "tab_name";
    public static final String TAB_NEWS = "news";
    public static final String TAB_BOOKMARK = "bookmark";
    private FragmentNewsBinding binding;
    private String tabName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            tabName = getArguments().getString(ARG_TAB);
        }

        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
        NewsViewModel viewModel = new ViewModelProvider(this, factory).get(NewsViewModel.class);

        NewsAdapter newsAdapter = new NewsAdapter(new OnItemClickCallback() {
            @Override
            public void onSaveClick(NewsEntity data) {
                viewModel.insertCourse(data);
            }

            @Override
            public void onDeleteClick(NewsEntity data) {
                viewModel.deleteCourse(data);
            }
        });

        if (tabName.equals(TAB_NEWS)) {
            viewModel.getHeadlineNews().observe(getViewLifecycleOwner(), courses -> {
                if (courses != null) {
                    if (courses instanceof Result.Loading){
                        binding.progressBar.setVisibility(View.VISIBLE);
                    } else if (courses instanceof Result.Success){
                        binding.progressBar.setVisibility(View.GONE);
                        newsAdapter.submitList(((Result.Success<List<NewsEntity>>) courses).getData());
                    } else if (courses instanceof Result.Error){
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Terjadi kesalahan"+ ((Result.Error<List<NewsEntity>>) courses).getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (tabName.equals(TAB_BOOKMARK)){
            viewModel.getBookmarks().observe(getViewLifecycleOwner(), courses -> {
                binding.progressBar.setVisibility(View.GONE);
                newsAdapter.submitList(courses);
            });
        }

        binding.rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNews.setHasFixedSize(true);
        binding.rvNews.setAdapter(newsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

