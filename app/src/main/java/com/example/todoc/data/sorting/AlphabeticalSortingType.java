package com.example.todoc.data.sorting;

import androidx.annotation.StringRes;

import com.example.todoc.R;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.Task;

import java.util.Comparator;

public enum AlphabeticalSortingType {

    AZ(
        R.string.sorting_alphabetic_sorted,
        (o1, o2) -> {
            return Integer.compare(o1.getProjectId(), o2.getProjectId());
        }
    ),
    ZA(
        R.string.sorting_alphabetic_inverted_sorted,
        (o1, o2) -> {
            return Integer.compare((o2.getProjectId()), o1.getProjectId());
        }
    ),
    NONE(
        R.string.sorting_alphabetic_none,
        null
    );

    @StringRes
    private int sortName;

    private Comparator<Task> comparator;

    AlphabeticalSortingType(int sortName, Comparator<Task> comparator) {
        this.sortName = sortName;
        this.comparator = comparator;
    }

    public int getSortName() {
        return sortName;
    }

    public Comparator<Task> getComparator() {
        return comparator;
    }
}
