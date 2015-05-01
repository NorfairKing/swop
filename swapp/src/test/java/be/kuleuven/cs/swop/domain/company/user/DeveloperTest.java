package be.kuleuven.cs.swop.domain.company.user;


import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.DateTimePeriod;


public class DeveloperTest {

	private Developer dev;
    @Before
    public void Setup() {
    	dev = new Developer("Name");
    }
	
    @Test
    public void ConstructorValidTest() {
        new Developer("Valid");
    }
    
    @Test
    public void NameTest(){
    	Developer dev2 = new Developer("Name With Spaces");
    	assertTrue("Name With Spaces".equals(dev2.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorInvalidTest() {
        new Developer(null);
    }
    
    @Test
    public void availableDuringTimeTest(){
    	assertTrue(dev.isAvailableDuring(LocalTime.of(7, 0)));
    	assertTrue(dev.isAvailableDuring(LocalTime.of(12, 0)));
    	assertTrue(dev.isAvailableDuring(LocalTime.of(17, 0)));
    	assertFalse(dev.isAvailableDuring(LocalTime.of(0, 0)));
    	assertFalse(dev.isAvailableDuring(LocalTime.of(6, 59)));
    	assertFalse(dev.isAvailableDuring(LocalTime.of(17, 1)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringTimeInvalidTest() {
    	LocalTime time = null;
    	dev.isAvailableDuring(time);
    }
    
    @Test
    public void availableDuringDateTimeTest(){
    	assertTrue(dev.isAvailableDuring(LocalDateTime.of(2015, 12, 31, 7, 0)));
    	assertTrue(dev.isAvailableDuring(LocalDateTime.of(2014, 1, 10, 12, 0)));
    	assertTrue(dev.isAvailableDuring(LocalDateTime.of(2015, 6, 8, 17, 0)));
    	assertFalse(dev.isAvailableDuring(LocalDateTime.of(2015, 8, 12, 0, 0)));
    	assertFalse(dev.isAvailableDuring(LocalDateTime.of(2015, 7, 31, 6, 59)));
    	assertFalse(dev.isAvailableDuring(LocalDateTime.of(2015, 12, 31, 17, 1)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringDateTimeInvalidTest() {
    	LocalDateTime time = null;
    	dev.isAvailableDuring(time);
    }
    
    @Test
    public void availableDuringDateTimePeriodTest(){
    	LocalDateTime time1 = LocalDateTime.of(2015, 4, 10, 4, 0); 
    	LocalDateTime time2 = LocalDateTime.of(2015, 4, 10, 6, 59); 
    	LocalDateTime time3 = LocalDateTime.of(2015, 4, 10, 7, 0); 
    	LocalDateTime time4 = LocalDateTime.of(2015, 4, 10, 12, 0); 
    	LocalDateTime time5 = LocalDateTime.of(2015, 4, 10, 17, 0); 
    	LocalDateTime time6 = LocalDateTime.of(2015, 4, 10, 17, 1); 
    	LocalDateTime time7 = LocalDateTime.of(2015, 4, 10, 22, 0); 
    	LocalDateTime time8 = LocalDateTime.of(2015, 4, 11, 12, 0); 
    	assertTrue(dev.isAvailableDuring(new  DateTimePeriod(time3, time5)));
    	assertTrue(dev.isAvailableDuring(new  DateTimePeriod(time4, time5)));
    	assertFalse(dev.isAvailableDuring(new  DateTimePeriod(time1, time4)));
    	assertFalse(dev.isAvailableDuring(new  DateTimePeriod(time2, time4)));
    	assertFalse(dev.isAvailableDuring(new  DateTimePeriod(time4, time6)));
    	assertFalse(dev.isAvailableDuring(new  DateTimePeriod(time4, time7)));
    	assertFalse(dev.isAvailableDuring(new  DateTimePeriod(time4, time8)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringDateTimePeriodInvalidTest() {
    	DateTimePeriod period = null;
    	dev.isAvailableDuring(period);
    }
    
    @Test
    public void canTakeBreakDuringTest(){
    	LocalDateTime time1 = LocalDateTime.of(2015, 4, 10, 6, 59); 
    	LocalDateTime time2 = LocalDateTime.of(2015, 4, 10, 7, 0); 
    	LocalDateTime time3 = LocalDateTime.of(2015, 4, 10, 11, 0); 
    	LocalDateTime time4 = LocalDateTime.of(2015, 4, 10, 12, 0); 
    	LocalDateTime time5 = LocalDateTime.of(2015, 4, 10, 14, 0); 
    	LocalDateTime time6 = LocalDateTime.of(2015, 4, 10, 14, 1); 
    	assertTrue(dev.canTakeBreakDuring(new  DateTimePeriod(time3, time4)));
    	assertTrue(dev.canTakeBreakDuring(new  DateTimePeriod(time3, time5)));
    	assertTrue(dev.canTakeBreakDuring(new  DateTimePeriod(time4, time5)));
    	assertFalse(dev.canTakeBreakDuring(new  DateTimePeriod(time1, time3)));
    	assertFalse(dev.canTakeBreakDuring(new  DateTimePeriod(time2, time4)));
    	assertFalse(dev.canTakeBreakDuring(new  DateTimePeriod(time4, time6)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void canTakeBreakDuringInvalidTest() {
    	Developer dev = new Developer("Name");
    	DateTimePeriod time = null;
    	dev.canTakeBreakDuring(time);
    }

}
