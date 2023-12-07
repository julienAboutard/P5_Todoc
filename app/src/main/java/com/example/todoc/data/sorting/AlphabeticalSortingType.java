package com.example.todoc.data.sorting;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.todoc.R;
import com.example.todoc.data.entity.ProjectWithTasks;

import java.util.Comparator;

public enum AlphabeticalSortingType {
    AZ(
        new int[]{-R.attr.state_not_sorted, R.attr.state_sorted, -R.attr.state_invert_sorted},
        R.string.sorting_alphabetic_sorted,
        (o1, o2) -> o1.getProject().getProjectName().compareTo(o2.getProject().getProjectName())
    ),
    ZA(
        new int[]{-R.attr.state_not_sorted, -R.attr.state_sorted, R.attr.state_invert_sorted},
        R.string.sorting_alphabetic_inverted_sorted,
        (o1, o2) -> o2.getProject().getProjectName().compareTo(o1.getProject().getProjectName())
    ),
    NONE(
        new int[]{R.attr.state_not_sorted, -R.attr.state_sorted, -R.attr.state_invert_sorted},
        R.string.sorting_alphabetic_none,
        null
    );

    private final int[] state;

    @StringRes
    private final int messageStringRes;

    @Nullable
    private final Comparator<ProjectWithTasks> comparator;

    AlphabeticalSortingType(int[] state, @StringRes int messageStringRes, @Nullable Comparator<ProjectWithTasks> comparator) {
        this.state = state;
        this.messageStringRes = messageStringRes;
        this.comparator = comparator;
    }

    public int[] getState() {
        return state;
    }

    @StringRes
    public int getMessageStringRes() {
        return messageStringRes;
    }

    @Nullable
    public Comparator<ProjectWithTasks> getComparator() {
        return comparator;
    }
}
