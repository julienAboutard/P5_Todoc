package com.example.todoc.ui.tasks.sort;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoc.data.sorting.AlphabeticalSortingType;
import com.example.todoc.data.sorting.ChronologicalSortingType;
import com.example.todoc.data.sorting.SortingParametersRepository;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SortViewModel extends ViewModel {

    @NonNull
    private final SortingParametersRepository sortingParametersRepository;

    /** @noinspection FieldCanBeLocal*/
    @NonNull
    private final Executor ioExecutor;

    private final MediatorLiveData<SortViewState> sortViewStateMediatorLiveData = new MediatorLiveData<>();

    @Inject
    public SortViewModel(@NonNull Executor ioExecutor,
                         @NonNull SortingParametersRepository sortingParametersRepository) {
        this.ioExecutor = ioExecutor;
        this.sortingParametersRepository = sortingParametersRepository;

        LiveData<AlphabeticalSortingType> alphabeticalSortingTypeLiveData = sortingParametersRepository.getAlphabeticalSortingTypeLiveData();
        LiveData<ChronologicalSortingType> chronologicalSortingTypeLiveData = sortingParametersRepository.getChronologicalSortingTypeLiveData();

        sortViewStateMediatorLiveData.addSource(alphabeticalSortingTypeLiveData, alphabeticalSortingType ->
            combine(alphabeticalSortingType, chronologicalSortingTypeLiveData.getValue())
        );

        sortViewStateMediatorLiveData.addSource(chronologicalSortingTypeLiveData, chronologicalSortingType ->
            combine(alphabeticalSortingTypeLiveData.getValue(), chronologicalSortingType)
        );
    }

    public void onAlphabeticalSortingClicked() {
        sortingParametersRepository.changeAlphabeticalSorting();
    }

    public void onChronologicalSortingClicked() {
        sortingParametersRepository.changeChronologicalSorting();
    }

    @NonNull
    public LiveData<SortViewState> getViewStateLiveData() {
        return sortViewStateMediatorLiveData;
    }

    private void combine(
        @Nullable AlphabeticalSortingType alphabeticalSortingType,
        @Nullable ChronologicalSortingType chronologicalSortingType
    ) {
        if (alphabeticalSortingType == null || chronologicalSortingType == null) {
            throw new IllegalStateException("At least one sorting type is NULL ! " +
                "alphabeticalSortingType = " + alphabeticalSortingType + "," +
                "chronologicalSortingType = " + chronologicalSortingType);
        }

        sortViewStateMediatorLiveData.setValue(
            new SortViewState(
                alphabeticalSortingType.getState(),
                alphabeticalSortingType.getMessageStringRes(),
                chronologicalSortingType.getState(),
                chronologicalSortingType.getMessageStringRes()
            )
        );
    }
}
