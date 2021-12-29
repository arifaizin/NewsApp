package com.dicoding.academies.ui.academy;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.academies.R;
import com.dicoding.academies.data.source.local.entity.CourseEntity;
import com.dicoding.academies.data.source.remote.response.ArticlesItem;
import com.dicoding.academies.databinding.ItemsAcademyBinding;

import java.util.ArrayList;
import java.util.List;

public class AcademyAdapter extends RecyclerView.Adapter<AcademyAdapter.CourseViewHolder> {

    private OnItemClickCallback onItemClickCallback;

    public AcademyAdapter(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    private List<CourseEntity> listCourses = new ArrayList<>();

    void setCourses(List<CourseEntity> listCourses) {
        if (listCourses == null) return;
        this.listCourses.clear();
        this.listCourses.addAll(listCourses);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemsAcademyBinding binding = ItemsAcademyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseViewHolder holder, int position) {
        CourseEntity course = listCourses.get(position);
        holder.bind(course);

        ImageView ivBookmark = holder.binding.ivBookmark;

        if (course.isBookmarked()) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_bookmarked_white));
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_bookmark_white));
        }

        ivBookmark.setOnClickListener(view -> {
            if (ivBookmark.getTag().toString().equals("Saved")) {
                ivBookmark.setTag("Not Saved");
                ivBookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_bookmark_white));
                onItemClickCallback.onDeleteClick(course);
            } else {
                ivBookmark.setTag("Saved");
                ivBookmark.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_bookmarked_white));
                onItemClickCallback.onSaveClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCourses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        final ItemsAcademyBinding binding;

        CourseViewHolder(ItemsAcademyBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(CourseEntity course) {
            binding.tvItemTitle.setText(course.getTitle());
            binding.tvItemDate.setText(String.format("Deadline %s", course.getDeadline()));
            itemView.setOnClickListener(v -> {

            });
            Glide.with(itemView.getContext())
                    .load(course.getImagePath())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(binding.imgPoster);


        }
    }
}

interface OnItemClickCallback {
    void onSaveClick(CourseEntity data);

    void onDeleteClick(CourseEntity data);
}