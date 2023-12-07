package com.example.todoc.ui.addtasks.spinner;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoc.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public SpinnerAdapter(@NonNull Context context, ArrayList<SpinnerItem> spinnerItemArrayList) {
        super(context, 0, spinnerItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_project_layout_resource, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinner_project_name);
        ImageView imageView = convertView.findViewById(R.id.spinner_project_color);
        SpinnerItem currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setColorFilter(currentItem.getProject().getProjectColor());
            textView.setText(currentItem.getProject().getProjectName());
        }
        return  convertView;
    }
}
