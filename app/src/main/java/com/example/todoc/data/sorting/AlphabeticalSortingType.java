package com.example.todoc.data.sorting;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.todoc.R;
import com.example.todoc.data.entity.Project;
import com.example.todoc.data.entity.Task;

import java.util.Comparator;

public enum AlphabeticalSortingType {

    /*AZ(
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
    );*/

    /*@StringRes
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
    }*/

    AZ(
        new int[]{-R.attr.state_not_sorted, R.attr.state_sorted, -R.attr.state_invert_sorted},
        R.string.sorting_alphabetic_sorted,
        (o1, o2) -> {
            return o1.getTask_name().compareTo(o2.getTask_name());
        }
    ),
    ZA(
        new int[]{-R.attr.state_not_sorted, -R.attr.state_sorted, R.attr.state_invert_sorted},
        R.string.sorting_alphabetic_inverted_sorted,
        (o1, o2) -> {
            return o2.getTask_name().compareTo(o1.getTask_name());
        }
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
    private final Comparator<Task> comparator;

    AlphabeticalSortingType(int[] state, @StringRes int messageStringRes, @Nullable Comparator<Task> comparator) {
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
