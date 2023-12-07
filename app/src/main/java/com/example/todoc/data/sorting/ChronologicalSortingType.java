package com.example.todoc.data.sorting;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.todoc.R;
import com.example.todoc.data.entity.Task;

import java.util.Comparator;

public enum ChronologicalSortingType {
    OLDEST_FIRST(
        new int[]{-R.attr.state_not_sorted, R.attr.state_sorted, -R.attr.state_invert_sorted},
        R.string.sorting_chronological_sorted,
        (o1, o2) -> Long.compare(o1.getTaskTimeStamp(), o2.getTaskTimeStamp())
    ),
    NEWEST_FIRST(
        new int[]{-R.attr.state_not_sorted, -R.attr.state_sorted, R.attr.state_invert_sorted},
        R.string.sorting_chronological_inverted_sorted,
        (o1, o2) -> Long.compare(o2.getTaskTimeStamp(), o1.getTaskTimeStamp())
    ),
    NONE(
        new int[]{R.attr.state_not_sorted, -R.attr.state_sorted, -R.attr.state_invert_sorted},
        R.string.sorting_chronological_none,
        null
    );

    private final int[] state;

    @StringRes
    private final int messageStringRes;

    @Nullable
    private final Comparator<Task> comparator;

    ChronologicalSortingType(int[] state, @StringRes int messageStringRes, @Nullable Comparator<Task> comparator) {
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
    public Comparator<Task> getComparator() {
        return comparator;
    }
}
