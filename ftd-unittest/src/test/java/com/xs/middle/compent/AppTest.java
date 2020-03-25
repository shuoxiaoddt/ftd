package com.xs.middle.compent;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.clear();
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }
}
