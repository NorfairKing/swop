package be.kuleuven.cs.swop.domain;


import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;


public class TimeCalculatorTest {

    @Test
    public void workingMinutesBetweenSimpleTest1() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 1, 8, 0)),
                0);
    }

    @Test
    public void workingMinutesBetweenSimpleTest2() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 8, 0),
                LocalDateTime.of(1990, 1, 1, 9, 0)),
                60);
    }

    @Test
    public void workingMinutesBetweenSimpleTest3() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 4, 0),
                LocalDateTime.of(1990, 1, 1, 10, 0)),
                2 * 60);
    }

    @Test
    public void workingMinutesBetweenSimpleTest4() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 5, 0),
                LocalDateTime.of(1990, 1, 1, 17, 0)),
                8 * 60);
    }

    @Test
    public void workingMinutesBetweenSimpleTest5() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 13, 0),
                LocalDateTime.of(1990, 1, 1, 18, 0)),
                3 * 60);
    }

    @Test
    public void workingMinutesBetweenSimpleTest6() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 15, 30),
                LocalDateTime.of(1990, 1, 1, 16, 0)),
                30);
    }

    @Test
    public void workingMinutesBetweenAdvancedTest1() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 2, 0, 0)),
                8 * 60);
    }

    @Test
    public void workingMinutesBetweenAdvancedTest2() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 2, 0, 30)),
                8 * 60);
    }

    @Test
    public void workingMinutesBetweenAdvancedTest3() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 2, 8, 30)),
                8 * 60 + 30);
    }

    @Test
    public void workingMinutesBetweenAdvancedTest4() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 7, 0),
                LocalDateTime.of(1990, 1, 2, 9, 0)),
                9 * 60);
    }

    @Test
    public void workingMinutesBetweenAdvancedTest5() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 15, 0),
                LocalDateTime.of(1990, 1, 2, 9, 0)),
                2 * 60);
    }

    @Test
    public void workingMinutesBetweenAdvancedTest6() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 12, 0),
                LocalDateTime.of(1990, 1, 2, 13, 0)),
                9 * 60);
    }

    @Test
    public void workingMinutesBetweenComplexTest1() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1970, 1, 2, 0, 0), // friday
                LocalDateTime.of(1970, 1, 6, 0, 0)),
                16 * 60); // over weekend
    }

    @Test
    public void workingMinutesBetweenComplexTest2() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1970, 1, 1, 8, 0), // thursday
                LocalDateTime.of(1970, 1, 7, 16, 0)),
                5 * 8 * 60);
    }

    @Test
    public void workingMinutesBetweenComplexTest3() {
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1970, 1, 1, 15, 30),
                LocalDateTime.of(1970, 1, 12, 9, 30)),
                30 + 8 * 60 + 5 * 8 * 60 + 90);
    }

}
