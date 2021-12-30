package com.dicoding.academies.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.academies.R;
import com.dicoding.academies.data.source.local.entity.NewsEntity;
import com.dicoding.academies.databinding.ItemNewsBinding;

public class NewsAdapter extends ListAdapter<NewsEntity, NewsAdapter.CourseViewHolder> {

    private final OnItemClickCallback onItemClickCallback;

    public NewsAdapter(OnItemClickCallback onItemClickCallback) {
        super(DIFF_CALLBACK);
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        NewsEntity course = getItem(position);
        holder.bind(course);

        ImageView ivBookmark = holder.binding.ivBookmark;

        if (course.isBookmarked()) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.getContext(), R.drawable.ic_bookmarked_white));
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.getContext(), R.drawable.ic_bookmark_white));
        }

        ivBookmark.setOnClickListener(view -> {
            if (course.isBookmarked()) {
                onItemClickCallback.onDeleteClick(course);
            } else {
                onItemClickCallback.onSaveClick(course);
            }
        });
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        final ItemNewsBinding binding;

        CourseViewHolder(ItemNewsBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(NewsEntity course) {
            binding.tvItemTitle.setText(course.getTitle());
            binding.tvItemDate.setText(course.getPublishedAt());
            itemView.setOnClickListener(v -> {

            });
            Glide.with(itemView.getContext())
                    .load(course.getImagePath())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(binding.imgPoster);
        }
    }

    public static final DiffUtil.ItemCallback<NewsEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NewsEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull NewsEntity oldUser, @NonNull NewsEntity newUser) {
                    return oldUser.getTitle().equals(newUser.getTitle());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull NewsEntity oldUser, @NonNull NewsEntity newUser) {
                    return oldUser.equals(newUser);
                }
            };
}

interface OnItemClickCallback {
    void onSaveClick(NewsEntity data);
    void onDeleteClick(NewsEntity data);
}