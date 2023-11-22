package com.example.todoc.data.sorting;

import androidx.annotation.StringRes;

import com.example.todoc.R;
import com.example.todoc.data.entity.Task;

import java.util.Comparator;

public enum ChronologicalSortingType {

    OLDEST_FIRST(
        R.string.sorting_chronological_sorted,
        (o1, o2) -> {
            return Long.compare(o1.getTask_timeStamp(), o2.getTask_timeStamp());
        }
    ),
    NEWEST_FIRST(
        R.string.sorting_chronological_inverted_sorted,
        (o1, o2) -> {
            return Long.compare(o2.getTask_timeStamp(), o1.getTask_timeStamp());
        }
    ),
    NONE(
        R.string.sorting_chronological_none,
        null
    );

    @StringRes
    private int sortName;

    private Comparator<Task> comparator;

    ChronologicalSortingType(int sortName, Comparator<Task> comparator) {
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
