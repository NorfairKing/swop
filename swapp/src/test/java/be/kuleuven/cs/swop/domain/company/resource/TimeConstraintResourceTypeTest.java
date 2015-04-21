package be.kuleuven.cs.swop.domain.company.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;

import org.junit.Test;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;


public class TimeConstraintResourceTypeTest {
    
    @Test
    public void ConstructionValidTest() {
        ResourceType type = new TimeConstrainedResourceType(
                "type", new HashSet<>(), new HashSet<>(), false,
                new TimePeriod(LocalTime.of(8, 0), LocalTime.of(10, 0)));

        assertTrue(type.isAvailableDuring(
                new DateTimePeriod(
                        LocalDateTime.of(2000, 1, 1, 8, 0),
                        LocalDateTime.of(2000, 1, 1, 10, 0)
            )));
        assertTrue(type.isAvailableDuring(
                new DateTimePeriod(
                        LocalDateTime.of(2000, 1, 1, 8, 30),
                        LocalDateTime.of(2000, 1, 1, 9, 0)
            )));
        assertFalse(type.isAvailableDuring(
                new DateTimePeriod(
                        LocalDateTime.of(2000, 1, 1, 7, 30),
                        LocalDateTime.of(2000, 1, 1, 9, 0)
            )));
        assertFalse(type.isAvailableDuring(
                new DateTimePeriod(
                        LocalDateTime.of(2000, 1, 1, 6, 30),
                        LocalDateTime.of(2000, 1, 1, 7, 30)
            )));
        
        assertTrue(type.isAvailableDuring(LocalTime.of(8, 0)));
        assertTrue(type.isAvailableDuring(LocalTime.of(8, 30)));
        assertTrue(type.isAvailableDuring(LocalTime.of(10, 0)));
        assertFalse(type.isAvailableDuring(LocalTime.of(10, 30)));
        assertFalse(type.isAvailableDuring(LocalTime.of(7, 30)));
    }
    
}
