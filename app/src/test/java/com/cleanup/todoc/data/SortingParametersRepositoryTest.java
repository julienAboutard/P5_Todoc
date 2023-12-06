package com.cleanup.todoc.data;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.example.todoc.data.sorting.AlphabeticalSortingType;
import com.example.todoc.data.sorting.ChronologicalSortingType;
import com.example.todoc.data.sorting.SortingParametersRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SortingParametersRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SortingParametersRepository sortingParametersRepository;

    @Before
    public void setUp() {
        sortingParametersRepository = new SortingParametersRepository();
    }

    @Test
    public void testInitialState() {
        //Given
        //When
        AlphabeticalSortingType alphabeticalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getAlphabeticalSortingTypeLiveData()
        );
        ChronologicalSortingType chronologicalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getChronologicalSortingTypeLiveData()
        );

        //Then
        assertEquals(AlphabeticalSortingType.NONE, alphabeticalSortingType);
        assertEquals(ChronologicalSortingType.NONE, chronologicalSortingType);
    }

    @Test
    public void testChangeAlphabeticalSorting() {
        //Given
        sortingParametersRepository.changeAlphabeticalSorting();

        //When
        AlphabeticalSortingType alphabeticalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getAlphabeticalSortingTypeLiveData()
        );

        //Then
        assertEquals(AlphabeticalSortingType.AZ, alphabeticalSortingType);

        //Given
        sortingParametersRepository.changeAlphabeticalSorting();

        //When
        alphabeticalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getAlphabeticalSortingTypeLiveData()
        );

        //Then
        assertEquals(AlphabeticalSortingType.ZA, alphabeticalSortingType);

        //Given
        sortingParametersRepository.changeAlphabeticalSorting();

        //When
        alphabeticalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getAlphabeticalSortingTypeLiveData()
        );

        //Then
        assertEquals(AlphabeticalSortingType.NONE, alphabeticalSortingType);

    }

    @Test
    public void testChangeChronologicalSorting() {
        //Given
        sortingParametersRepository.changeChronologicalSorting();

        //When
        ChronologicalSortingType chronologicalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getChronologicalSortingTypeLiveData()
        );

        //Then
        assertEquals(ChronologicalSortingType.OLDEST_FIRST, chronologicalSortingType);

        //Given
        sortingParametersRepository.changeChronologicalSorting();

        //When
        chronologicalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getChronologicalSortingTypeLiveData()
        );

        //Then
        assertEquals(ChronologicalSortingType.NEWEST_FIRST, chronologicalSortingType);

        //Given
        sortingParametersRepository.changeChronologicalSorting();

        //When
        chronologicalSortingType = LiveDataTestUtils.getValueForTesting(
            sortingParametersRepository.getChronologicalSortingTypeLiveData()
        );

        //Then
        assertEquals(ChronologicalSortingType.NONE, chronologicalSortingType);
    }

}
