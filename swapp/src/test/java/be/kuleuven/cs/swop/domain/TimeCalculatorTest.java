package be.kuleuven.cs.swop.domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TimeCalculatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {}
    
    @Test
    public void workingMinutesBetweenSimpleTest(){
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 1, 8, 0)),
                0);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 8, 0),
                LocalDateTime.of(1990, 1, 1, 9, 0)),
                60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 4, 0),
                LocalDateTime.of(1990, 1, 1, 10, 0)),
                2*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 5, 0),
                LocalDateTime.of(1990, 1, 1, 17, 0)),
                8*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 13, 0),
                LocalDateTime.of(1990, 1, 1, 18, 0)),
                3*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 15, 30),
                LocalDateTime.of(1990, 1, 1, 16, 0)),
                30);
    }
    @Test
    public void workingMinutesBetweenAdvancedTest(){
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 2, 0, 0)),
                8*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 2, 0, 30)),
                8*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(1990, 1, 2, 8, 30)),
                8*60 + 30);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 7, 0),
                LocalDateTime.of(1990, 1, 2, 9, 0)),
                9*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 15, 0),
                LocalDateTime.of(1990, 1, 2, 9, 0)),
                2*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1990, 1, 1, 12, 0),
                LocalDateTime.of(1990, 1, 2, 13, 0)),
                9*60);
    }
    @Test
    public void workingMinutesBetweenComplexTest(){
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1970, 1, 2, 0, 0), //friday
                LocalDateTime.of(1970, 1, 6, 0, 0)),
                16*60); //over weekend
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1970, 1, 1, 8, 0), //thursday
                LocalDateTime.of(1970, 1, 7, 16, 0)),
                5*8*60);
        assertEquals(TimeCalculator.workingMinutesBetween(
                LocalDateTime.of(1970, 1, 1, 15, 30),
                LocalDateTime.of(1970, 1, 12, 9, 30)),
                30 + 8*60 + 5*8*60 + 90);
    }


}
