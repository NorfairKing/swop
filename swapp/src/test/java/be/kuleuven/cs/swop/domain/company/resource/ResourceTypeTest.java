package be.kuleuven.cs.swop.domain.company.resource;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;


public class ResourceTypeTest {

    private ResourceType      type1;
    private ResourceType      type2;
    private ResourceType      type3;
    private ResourceType      type4;
    private Set<ResourceType> set1;
    private Set<ResourceType> set2;
    private Set<ResourceType> set3;
    private Set<ResourceType> setWithNullValue;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        type1 = new ResourceType("type1", new HashSet<ResourceType>(), new HashSet<ResourceType>(), false);
        Set<ResourceType> dep2 = new HashSet<ResourceType>();
        dep2.add(type1);
        type2 = new ResourceType("type2", dep2, new HashSet<ResourceType>(), true);
        type3 = new ResourceType("type3", new HashSet<ResourceType>(), dep2, false);
        type4 = new ResourceType("type4", dep2, dep2, true);

        set1 = new HashSet<>();
        set1.add(type2);
        set2 = new HashSet<>();
        set2.add(type1);
        set2.add(type3);
        set3 = new HashSet<>();
        set3.add(type4);
        set3.add(type2);
        set3.add(type1);
        setWithNullValue = new HashSet<>();
        setWithNullValue.add(null);
        setWithNullValue.add(type1);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void ConstructionValidTest() {
        ArrayList<Set<ResourceType>> validDeps = new ArrayList<>();
        validDeps.add(null);
        validDeps.add(new HashSet<>());
        validDeps.add(set1);
        validDeps.add(set2);
        validDeps.add(set3);

        ArrayList<Set<ResourceType>> validConflicts = new ArrayList<>();
        validConflicts.add(null);
        validConflicts.add(new HashSet<>());
        validConflicts.add(set1);
        validConflicts.add(set2);
        validConflicts.add(set3);

        for (Set<ResourceType> deps : validDeps) {
            for (Set<ResourceType> conflicts : validConflicts) {
                new ResourceType("valid", deps, conflicts, false);
                new ResourceType("valid", deps, conflicts, true);
            }
        }

    }
    
    @Test
    public void nameTest(){
    	assertTrue("type1".equals(type1.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructionInvalidNameTest() {
        new ResourceType(null, set1, set2, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructionInvalidDependenciesTest() {
        new ResourceType("Valid", setWithNullValue, set1, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructionInvalidConflictsTest() {
        new ResourceType("Valid", set2, setWithNullValue, false);
    }
    
    @Test
    public void availableDuringTimeTest(){
    	assertTrue(type1.isAvailableDuring(LocalTime.of(7, 0)));
    	assertTrue(type1.isAvailableDuring(LocalTime.of(12, 0)));
    	assertTrue(type1.isAvailableDuring(LocalTime.of(17, 0)));
    	assertTrue(type1.isAvailableDuring(LocalTime.of(0, 0)));
    	assertTrue(type1.isAvailableDuring(LocalTime.of(6, 59)));
    	assertTrue(type1.isAvailableDuring(LocalTime.of(17, 1)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringTimeInvalidTest() {
    	LocalTime time = null;
    	type1.isAvailableDuring(time);
    }
    
    @Test
    public void availableDuringDateTimeTest(){
    	assertTrue(type1.isAvailableDuring(LocalDateTime.of(2015, 12, 31, 7, 0)));
    	assertTrue(type1.isAvailableDuring(LocalDateTime.of(2014, 1, 10, 12, 0)));
    	assertTrue(type1.isAvailableDuring(LocalDateTime.of(2015, 6, 8, 17, 0)));
    	assertTrue(type1.isAvailableDuring(LocalDateTime.of(2015, 8, 12, 0, 0)));
    	assertTrue(type1.isAvailableDuring(LocalDateTime.of(2015, 7, 31, 6, 59)));
    	assertTrue(type1.isAvailableDuring(LocalDateTime.of(2015, 12, 31, 17, 1)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringDateTimeInvalidTest() {
    	LocalDateTime time = null;
    	type1.isAvailableDuring(time);
    }
    
    @Test
    public void availableDuringTimePeriodTest(){
    	LocalTime time1 = LocalTime.of(4, 0); 
    	LocalTime time2 = LocalTime.of(6, 59); 
    	LocalTime time3 = LocalTime.of(7, 0); 
    	LocalTime time4 = LocalTime.of(12, 0); 
    	LocalTime time5 = LocalTime.of(17, 0); 
    	LocalTime time6 = LocalTime.of(17, 1); 
    	LocalTime time7 = LocalTime.of(22, 0); 
    	assertTrue(type1.isAvailableDuring(new TimePeriod(time3, time5)));
    	assertTrue(type1.isAvailableDuring(new TimePeriod(time4, time5)));
    	assertTrue(type1.isAvailableDuring(new TimePeriod(time1, time4)));
    	assertTrue(type1.isAvailableDuring(new TimePeriod(time2, time4)));
    	assertTrue(type1.isAvailableDuring(new TimePeriod(time4, time6)));
    	assertTrue(type1.isAvailableDuring(new TimePeriod(time4, time7)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringTimePeriodInvalidTest() {
    	TimePeriod period = null;
    	type1.isAvailableDuring(period);
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
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time3, time5)));
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time4, time5)));
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time1, time4)));
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time2, time4)));
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time4, time6)));
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time4, time7)));
    	assertTrue(type1.isAvailableDuring(new  DateTimePeriod(time4, time8)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void availableDuringDateTimePeriodInvalidTest() {
    	DateTimePeriod period = null;
    	type1.isAvailableDuring(period);
    }
    

}
