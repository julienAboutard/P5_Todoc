package com.example.todoc.ui.addtasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoc.data.entity.Project;
import com.example.todoc.databinding.DialogAddTaskBinding;
import com.example.todoc.ui.addtasks.spinner.SpinnerAdapter;
import com.example.todoc.ui.addtasks.spinner.SpinnerItem;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTaskDialogFragment extends DialogFragment {

    @NonNull
    public static AddTaskDialogFragment newInstance() {
        return new AddTaskDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogAddTaskBinding binding = DialogAddTaskBinding.inflate(getLayoutInflater());
        AddTaskViewModel viewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);

        viewModel.getAllProjects().observe(this, projects -> {
            ArrayList<SpinnerItem> spinnerItemArrayList = new ArrayList<>();

            for (Project project : projects) {
                spinnerItemArrayList.add(new SpinnerItem(project));
            }

            final SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), spinnerItemArrayList);
            binding.projectSpinner.setAdapter(adapter);
            binding.projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SpinnerItem spinnerItem = (SpinnerItem) parent.getItemAtPosition(position);

                    viewModel.onProjectSelected(spinnerItem.getProject());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });

        binding.txtTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onTaskNameChanged(s.toString());
            }
        });
        binding.buttonCancel.setOnClickListener(v -> viewModel.onCancelButtonClicked());
        binding.buttonOk.setOnClickListener(v -> viewModel.onAddButtonClicked(
            System.currentTimeMillis()
        ));

        viewModel.getCloseDialogSingleLiveEvent().observe(this, ignored ->
            dismiss()
        );

        viewModel.getDisplayToastSingleLiveEvent().observe(this, stringResId ->
            Toast.makeText(requireContext(), stringResId, Toast.LENGTH_SHORT).show()
        );

        return binding.getRoot();
    }
}
