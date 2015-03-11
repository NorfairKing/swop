package be.kuleuven.cs.swop.domain;


import static org.junit.Assert.assertNotEquals;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TimePeriodTest {

    private static TimePeriod validTimePeriod1;
    private static TimePeriod validTimePeriod2;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        validTimePeriod1 = new TimePeriod(new Date(1), new Date(2));
        validTimePeriod2 = new TimePeriod(new Date(3), new Date(4));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void constructorValidTest() {
        new TimePeriod(new Date(1), new Date(2));
        new TimePeriod(new Date(3), new Date(4));
        new TimePeriod(new Date(5), new Date(6));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void constructorInvalidTest() {
        exception.expect(IllegalArgumentException.class);
        new TimePeriod(new Date(1), new Date(0));

        exception.expect(IllegalArgumentException.class);
        new TimePeriod(null, new Date(0));

        exception.expect(IllegalArgumentException.class);
        new TimePeriod(new Date(1), null);
    }

    @Test
    public void canHaveAsStartTimeValidTest() {
        validTimePeriod1.canHaveAsStartTime(new Date(0));
        validTimePeriod1.canHaveAsStartTime(new Date(5));
    }

    @Test
    public void canHaveAsStartTimeInalidTest() {
        validTimePeriod1.canHaveAsStartTime(null);
    }

    @Test
    public void canHaveAsStopTimeValidTest() {
        validTimePeriod1.canHaveAsStopTime(new Date(3));
        validTimePeriod1.canHaveAsStopTime(new Date(4));
    }

    @Test
    public void canHaveAsStopTimeInvalidTest() {
        validTimePeriod1.canHaveAsStopTime(null);
        validTimePeriod1.canHaveAsStopTime(new Date(0));
        validTimePeriod1.canHaveAsStopTime(new Date(3));
        validTimePeriod1.canHaveAsStopTime(new Date(2));
    }

    public void getStartTimeTest() {
        Date startTime = new Date(1);
        TimePeriod t = new TimePeriod(startTime, new Date(2));
        assertNotEquals(t.getStartTime(), startTime);
    }

    public void getStopTimeTest() {
        Date stopTime = new Date(2);
        TimePeriod t = new TimePeriod(new Date(1), stopTime);
        assertNotEquals(t.getStartTime(), stopTime);
    }

}
