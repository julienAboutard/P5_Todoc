package com.example.todoc.ui.tasks;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoc.databinding.ItemTaskBinding;

public class TaskAdapter extends ListAdapter<TaskViewStateItem, TaskAdapter.ViewHolder> {

    private final TaskListener taskListener;

    public TaskAdapter(TaskListener taskListener) {
        super(new TaskCallback());
        this.taskListener = taskListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
            )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), taskListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTaskBinding binding;

        public ViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TaskViewStateItem item, TaskListener taskListener) {
            binding.imgProject.setColorFilter(item.getProjectColor());
            binding.lblTaskName.setText(item.getTaskName());
            binding.lblProjectName.setText(item.getProjectName());
            binding.imgDelete.setOnClickListener(v -> taskListener.onDeleteTaskButtonClicked(item.getTaskId()));
        }
    }

    private static class TaskCallback extends DiffUtil.ItemCallback<TaskViewStateItem> {
        @Override
        public boolean areItemsTheSame(@NonNull TaskViewStateItem oldItem, @NonNull TaskViewStateItem newItem) {
            return oldItem.getTaskId() == newItem.getTaskId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskViewStateItem oldItem, @NonNull TaskViewStateItem newItem) {
            return oldItem.getTaskName().equals(newItem.getTaskName()) &&
                oldItem.getProjectColor() == newItem.getProjectColor() &&
                oldItem.getProjectName().equals(newItem.getProjectName());
        }
    }
}
