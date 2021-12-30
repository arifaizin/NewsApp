package com.dicoding.academies.ui;

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

import com.dicoding.academies.data.source.local.entity.NewsEntity;
import com.dicoding.academies.databinding.FragmentNewsBinding;
import com.dicoding.academies.viewmodel.ViewModelFactory;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        viewModel.getHeadlineNews().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null) {
                switch (courses.status) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        newsAdapter.submitList(courses.data);
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

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

