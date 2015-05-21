package be.kuleuven.cs.swop.domain;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;


public class DateTimePeriodTest {

    private static DateTimePeriod validTimePeriod1;
    private static LocalDateTime now = LocalDateTime.now();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        validTimePeriod1 = new DateTimePeriod(now.plusHours(1), now.plusHours(2));
    }

    @Test
    public void constructorValidTest1() {
        new DateTimePeriod(now.plusHours(1), now.plusHours(2));
    }
    @Test
    public void constructorValidTest2() {
        new DateTimePeriod(now.minusHours(1), now.plusHours(1));
    }
    @Test
    public void constructorValidTest3() {
        new DateTimePeriod(now, now.plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorCreationBeforeEndTimeTest() {
        new DateTimePeriod(now.plusHours(2), now.plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidStartTimeTest() {
        new DateTimePeriod(null, now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEndTimeTest() {
        new DateTimePeriod(now, null);
    }

}
