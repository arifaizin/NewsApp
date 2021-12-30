package com.dicoding.newsapp.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dicoding.newsapp.data.source.local.entity.NewsEntity;
import com.dicoding.newsapp.databinding.FragmentBookmarkBinding;
import com.dicoding.newsapp.viewmodel.ViewModelFactory;

public class BookmarkFragment extends Fragment {

    private FragmentBookmarkBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
            NewsViewModel viewModel = new ViewModelProvider(this, factory).get(NewsViewModel.class);

            NewsAdapter adapter = new NewsAdapter(new OnItemClickCallback() {
                @Override
                public void onSaveClick(NewsEntity data) {
                    viewModel.insertCourse(data);
                }

                @Override
                public void onDeleteClick(NewsEntity data) {
                    viewModel.deleteCourse(data);
                }
            });
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.getBookmarks().observe(getViewLifecycleOwner(), courses -> {
                binding.progressBar.setVisibility(View.GONE);
                adapter.submitList(courses);
            });

            binding.rvBookmark.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvBookmark.setHasFixedSize(true);
            binding.rvBookmark.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}