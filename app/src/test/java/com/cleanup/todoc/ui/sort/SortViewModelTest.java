package com.cleanup.todoc.ui.sort;

import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.utils.TestExecutor;
import com.example.todoc.data.sorting.SortingParametersRepository;
import com.example.todoc.ui.tasks.sort.SortViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class SortViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock private SortingParametersRepository sortingParametersRepository;
    private final Executor ioExecutor = Mockito.spy(new TestExecutor());

    private SortViewModel viewModel;

    @Before
    public void setUp() {
        Mockito.doReturn(new MutableLiveData<>()).when(sortingParametersRepository).getAlphabeticalSortingTypeLiveData();
        Mockito.doReturn(new MutableLiveData<>()).when(sortingParametersRepository).getChronologicalSortingTypeLiveData();

        viewModel = new SortViewModel(ioExecutor, sortingParametersRepository);
    }

    @Test
    public void verify_alphabetical_sorting_click() {
        // When
        viewModel.onAlphabeticalSortingClicked();

        // Then
        verify(sortingParametersRepository).getAlphabeticalSortingTypeLiveData();
        verify(sortingParametersRepository).getChronologicalSortingTypeLiveData();
        verify(sortingParametersRepository).changeAlphabeticalSorting();
        Mockito.verifyNoMoreInteractions(sortingParametersRepository);
    }

    @Test
    public void verify_chronological_sorting_click() {
        // When
        viewModel.onChronologicalSortingClicked();

        // Then
        verify(sortingParametersRepository).getAlphabeticalSortingTypeLiveData();
        verify(sortingParametersRepository).getChronologicalSortingTypeLiveData();
        verify(sortingParametersRepository).changeChronologicalSorting();
        Mockito.verifyNoMoreInteractions(sortingParametersRepository);
    }
}
