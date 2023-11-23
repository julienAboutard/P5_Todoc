package com.example.todoc.ui.addtasks.spinner;


import com.example.todoc.data.entity.Project;

public class SpinnerItem {

    private Project project;

    public SpinnerItem(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
