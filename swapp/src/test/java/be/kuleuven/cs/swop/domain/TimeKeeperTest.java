package be.kuleuven.cs.swop.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TimeKeeperTest {
    private static int minuteMillis= 60 * 1000;
    private static int hourMillis = 60 * minuteMillis;
    private static int dayMillis = 24 * hourMillis;

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
        assertEquals(Timekeeper.workingMinutesBetween(new Date(0), new Date(8 * hourMillis)),0);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(8 * hourMillis), new Date(9 * hourMillis)),60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(4 * hourMillis), new Date(10 * hourMillis)),2*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(4 * hourMillis), new Date(10 * hourMillis)),2*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(5 * hourMillis), new Date(17 * hourMillis)),8*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(13 * hourMillis), new Date(18 * hourMillis)),3*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(15 * hourMillis + 30 * minuteMillis), new Date(16 * hourMillis)),30);
    }
    @Test
    public void workingMinutesBetweenAdvancedTest(){
        assertEquals(Timekeeper.workingMinutesBetween(new Date(0), new Date(dayMillis)),8*60);
        System.out.println("test");
        assertEquals(Timekeeper.workingMinutesBetween(new Date(0), new Date(dayMillis + 30 * minuteMillis)),8*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(0), new Date(dayMillis + 8 * hourMillis +30 * minuteMillis)),8*60 + 30);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(7 * hourMillis), new Date(dayMillis + 9 * hourMillis)),9*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(15 * hourMillis), new Date(dayMillis + 9 * hourMillis)),2*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(12 * hourMillis), new Date(dayMillis + 13 * hourMillis)),9*60);
    }
    @Test
    public void workingMinutesBetweenComplexTest(){
        assertEquals(Timekeeper.workingMinutesBetween(new Date(dayMillis), new Date( 5 * dayMillis)),16*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(8 * hourMillis), new Date(6 * dayMillis + 16 * hourMillis)),5*8*60);
        assertEquals(Timekeeper.workingMinutesBetween(new Date(15 * hourMillis + 30 * minuteMillis), new Date(11 * dayMillis + 9 * hourMillis +30 * minuteMillis)),30 + 8*60 + 5*8*60 + 90);

    }


}
