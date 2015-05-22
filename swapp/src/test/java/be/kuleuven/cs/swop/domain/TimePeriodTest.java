package be.kuleuven.cs.swop.domain;


import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import org.junit.BeforeClass;
import org.junit.Test;


public class TimePeriodTest {

    private static TimePeriod validTimePeriod1;
    private static LocalTime now = LocalTime.now();
    private static LocalTime noon          = LocalTime.NOON;
    private static LocalDateTime noonDate          = LocalDateTime.of(LocalDate.now(), noon);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        validTimePeriod1 = new TimePeriod(noon.plusHours(1), noon.plusHours(2));
    }

    @Test
    public void constructorValidTest1() {
        new TimePeriod(now.plusHours(1), now.plusHours(2));
    }
    @Test
    public void constructorValidTest2() {
        new TimePeriod(now.minusHours(1), now.plusHours(1));
    }
    @Test
    public void constructorValidTest3() {
        new TimePeriod(now, now.plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorCreationBeforeEndTimeTest() {
        new TimePeriod(now.plusHours(2), now.plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidStartTimeTest() {
        new TimePeriod(null, now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEndTimeTest() {
        new TimePeriod(now, null);
    }
    
    @Test
    public void isDuringPeriodTest(){
        assertTrue(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(65), noon.plusMinutes(110))));
        assertTrue(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(60), noon.plusMinutes(120))));
        assertTrue(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(65), noon.plusMinutes(120))));
        assertTrue(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(60), noon.plusMinutes(110))));
        assertFalse(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(55), noon.plusMinutes(120))));
        assertFalse(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(55), noon.plusMinutes(100))));
        assertFalse(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(70), noon.plusMinutes(125))));
        assertFalse(validTimePeriod1.isDuring(new TimePeriod(noon.plusMinutes(60), noon.plusMinutes(125))));
    }
    
    @Test
    public void isDuringDateTimePeriodTest(){
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(65), noonDate.plusMinutes(110))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(60), noonDate.plusMinutes(120))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(65), noonDate.plusMinutes(120))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(60), noonDate.plusMinutes(110))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(55), noonDate.plusMinutes(120))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(55), noonDate.plusMinutes(100))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(70), noonDate.plusMinutes(125))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusMinutes(60), noonDate.plusMinutes(125))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(noonDate.plusDays(1).plusMinutes(65), noonDate.plusDays(1).plusMinutes(110))));
    }
    @Test
    public void isDuringLocalTimeTest(){
        assertTrue(validTimePeriod1.isDuring(noon.plusMinutes(65)));
        assertTrue(validTimePeriod1.isDuring(noon.plusMinutes(60)));
        assertTrue(validTimePeriod1.isDuring(noon.plusMinutes(120)));
        assertFalse(validTimePeriod1.isDuring(noon.plusMinutes(55)));
        assertFalse(validTimePeriod1.isDuring(noon.plusMinutes(125)));
    }

}
