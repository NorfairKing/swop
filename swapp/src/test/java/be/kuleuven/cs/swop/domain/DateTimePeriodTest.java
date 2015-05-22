package be.kuleuven.cs.swop.domain;


import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.BeforeClass;
import org.junit.Test;


public class DateTimePeriodTest {

    private static DateTimePeriod validTimePeriod1;
    private static LocalDateTime now = LocalDateTime.now();
    private static LocalDateTime epoch          = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        validTimePeriod1 = new DateTimePeriod(epoch.plusHours(1), epoch.plusHours(2));
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
    
    @Test
    public void isDuringPeriodTest(){
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(65), epoch.plusMinutes(110))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(60), epoch.plusMinutes(120))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(65), epoch.plusMinutes(120))));
        assertTrue(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(60), epoch.plusMinutes(110))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(55), epoch.plusMinutes(120))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(55), epoch.plusMinutes(100))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(70), epoch.plusMinutes(125))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusMinutes(60), epoch.plusMinutes(125))));
        assertFalse(validTimePeriod1.isDuring(new DateTimePeriod(epoch.plusDays(1).plusMinutes(65), epoch.plusDays(1).plusMinutes(110))));
    }
    
    @Test
    public void isDuringLocalDateTimeExcludeExtremesTest(){
        assertTrue(validTimePeriod1.isDuringExcludeExtremes(epoch.plusMinutes(65)));
        assertFalse(validTimePeriod1.isDuringExcludeExtremes(epoch.plusMinutes(60)));
        assertFalse(validTimePeriod1.isDuringExcludeExtremes(epoch.plusMinutes(120)));
        assertFalse(validTimePeriod1.isDuringExcludeExtremes(epoch.plusMinutes(55)));
        assertFalse(validTimePeriod1.isDuringExcludeExtremes(epoch.plusMinutes(125)));
        assertFalse(validTimePeriod1.isDuringExcludeExtremes(epoch.plusDays(1).plusMinutes(80)));
    }
    @Test
    public void isDuringLocalDateTimeTest(){
        assertTrue(validTimePeriod1.isDuring(epoch.plusMinutes(65)));
        assertTrue(validTimePeriod1.isDuring(epoch.plusMinutes(60)));
        assertTrue(validTimePeriod1.isDuring(epoch.plusMinutes(120)));
        assertFalse(validTimePeriod1.isDuring(epoch.plusMinutes(55)));
        assertFalse(validTimePeriod1.isDuring(epoch.plusMinutes(125)));
        assertFalse(validTimePeriod1.isDuring(epoch.plusDays(1).plusMinutes(80)));
    }
    
    @Test
    public void overlaps(){
        assertTrue(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusMinutes(65), epoch.plusMinutes(110))));
        assertTrue(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusMinutes(60), epoch.plusMinutes(120))));
        assertTrue(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusMinutes(40), epoch.plusMinutes(65))));
        assertFalse(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusMinutes(40), epoch.plusMinutes(60))));
        assertTrue(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusMinutes(110), epoch.plusMinutes(140))));
        assertFalse(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusMinutes(120), epoch.plusMinutes(140))));
        assertFalse(validTimePeriod1.overlaps(new DateTimePeriod(epoch.plusDays(1).plusMinutes(65), epoch.plusDays(1).plusMinutes(110))));
    }


}
