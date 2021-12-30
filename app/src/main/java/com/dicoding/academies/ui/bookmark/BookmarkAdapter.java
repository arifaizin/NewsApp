package com.dicoding.academies.ui.bookmark;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.academies.R;
import com.dicoding.academies.data.source.local.entity.NewsEntity;
import com.dicoding.academies.databinding.ItemsBookmarkBinding;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.CourseViewHolder> {
    private final BookmarkFragmentCallback callback;
    private ArrayList<NewsEntity> listCourses = new ArrayList<>();

    BookmarkAdapter(BookmarkFragmentCallback callback) {
        this.callback = callback;
    }


    public void setCourses(List<NewsEntity> courses) {
        if (courses == null) return;
        this.listCourses.clear();
        this.listCourses.addAll(courses);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemsBookmarkBinding binding = ItemsBookmarkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CourseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseViewHolder holder, int position) {
        NewsEntity course = listCourses.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return listCourses.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        final ItemsBookmarkBinding binding;

        CourseViewHolder(ItemsBookmarkBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(NewsEntity course) {
            binding.tvItemTitle.setText(course.getTitle());
            binding.tvItemDate.setText(String.format("Deadline %s", course.getPublishedAt()));
            itemView.setOnClickListener(v -> {
            });
            binding.imgShare.setOnClickListener(v -> callback.onShareClick(course));
            Glide.with(itemView.getContext())
                    .load(course.getImagePath())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(binding.imgPoster);
        }
    }
}