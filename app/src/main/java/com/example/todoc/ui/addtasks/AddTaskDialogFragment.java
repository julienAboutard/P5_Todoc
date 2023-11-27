package com.example.todoc.ui.addtasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoc.data.entity.Project;
import com.example.todoc.databinding.DialogAddTaskBinding;
import com.example.todoc.ui.addtasks.spinner.SpinnerAdapter;
import com.example.todoc.ui.addtasks.spinner.SpinnerItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTaskDialogFragment extends DialogFragment {

    @NonNull
    public static AddTaskDialogFragment newInstance() {
        return new AddTaskDialogFragment();
    }

    @NonNull
    private ArrayList<SpinnerItem> spinnerItemArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogAddTaskBinding binding = DialogAddTaskBinding.inflate(LayoutInflater.from(requireContext()));
        AddTaskViewModel viewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);

        initSpinner(viewModel.getAllProjects());

        final SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), spinnerItemArrayList);
        binding.projectSpinner.setAdapter(adapter);
        binding.projectSpinner.setOnItemClickListener((parent, view, position, id) ->
            viewModel.onProjectSelected(adapter.getItem(position).getProject())
        );
        binding.txtTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onTaskName(s.toString());
            }
        });
        binding.buttonCancel.setOnClickListener(v -> viewModel.onCancelButtonClicked());
        binding.buttonOk.setOnClickListener(v -> viewModel.onAddButtonClicked(
            System.currentTimeMillis()
        ));

        viewModel.getCloseActivitySingleLiveEvent().observe(this, ignored ->
            dismiss()
        );

        /*viewModel.getAddTaskViewStateLiveData().observe(this, addTaskViewState -> {
            adapter.clear();
            adapter.addAll(addTaskViewState.getAddTaskViewStateItems());

            binding.createTaskButtonOk.setVisibility(addTaskViewState.isProgressBarVisible() ? View.INVISIBLE : View.VISIBLE);
            binding.createTaskProgressBarOk.setVisibility(addTaskViewState.isProgressBarVisible() ? View.VISIBLE : View.INVISIBLE);
        });

        viewModel.getDismissDialogSingleLiveEvent().observe(this, ignored ->
            dismiss()
        );
        viewModel.getDisplayToastMessageSingleLiveEvent().observe(this, message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        );*/

        return binding.getRoot();
    }

    private void initSpinner(List<Project> projects) {
        for (Project project : projects) {
            spinnerItemArrayList.add(new SpinnerItem(project));
        }
    }
}
