package be.kuleuven.cs.swop.domain;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;


public class TimePeriodTest {

    private static DateTimePeriod validTimePeriod1;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        validTimePeriod1 = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
    }

    @Test
    public void constructorValidTest1() {
        new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
    }
    @Test
    public void constructorValidTest2() {
        new DateTimePeriod(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }
    @Test
    public void constructorValidTest3() {
        new DateTimePeriod(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorCreationBeforeEndTimeTest() {
        new DateTimePeriod(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidStartTimeTest() {
        new DateTimePeriod(null, LocalDateTime.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEndTimeTest() {
        new DateTimePeriod(LocalDateTime.now(), null);
    }

    @Test
    public void canHaveAsStartTimeValidTest() {
        assertTrue(validTimePeriod1.canHaveAsStartTime(LocalDateTime.now()));
        assertTrue(validTimePeriod1.canHaveAsStartTime(LocalDateTime.now().plusHours(5)));
    }

    @Test
    public void canHaveAsStartTimeInvalidTest() {
        assertFalse(validTimePeriod1.canHaveAsStartTime(null));
    }

    @Test
    public void canHaveAsStopTimeValidTest() {
        assertTrue(validTimePeriod1.canHaveAsStopTime(LocalDateTime.now().plusHours(1))); // FIXME: Heisenbug. This goes wrong sometimes when not debugging, but works perfectly while debugging
        assertTrue(validTimePeriod1.canHaveAsStopTime(LocalDateTime.now().plusHours(3)));
    }

    @Test
    public void canHaveAsStopTimeInvalidTest() {
        assertFalse(validTimePeriod1.canHaveAsStopTime(null));
        assertFalse(validTimePeriod1.canHaveAsStopTime(LocalDateTime.now()));
    }

}
