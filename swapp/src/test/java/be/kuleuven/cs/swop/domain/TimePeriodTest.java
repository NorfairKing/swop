package be.kuleuven.cs.swop.domain;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TimePeriodTest {

    private static TimePeriod validTimePeriod1;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        validTimePeriod1 = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void constructorValidTest() {
        new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        
        new TimePeriod(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        
        new TimePeriod(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void constructorInvalidTest() {
        try{
            new TimePeriod(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(1)); 
        }catch(IllegalArgumentException e){}

        try{
            new TimePeriod(null, LocalDateTime.now());
        }catch(IllegalArgumentException e){}

        try{
            new TimePeriod(LocalDateTime.now(), null);
        }catch(IllegalArgumentException e){}
    }

    @Test
    public void canHaveAsStartTimeValidTest() {
        validTimePeriod1.canHaveAsStartTime(LocalDateTime.now());
        validTimePeriod1.canHaveAsStartTime(LocalDateTime.now().plusHours(5));
    }

    @Test
    public void canHaveAsStartTimeInvalidTest() {
        validTimePeriod1.canHaveAsStartTime(null);
    }

    @Test
    public void canHaveAsStopTimeValidTest() {
        validTimePeriod1.canHaveAsStopTime(LocalDateTime.now().plusHours(1));
        validTimePeriod1.canHaveAsStopTime(LocalDateTime.now().plusHours(3));
    }

    @Test
    public void canHaveAsStopTimeInvalidTest() {
        validTimePeriod1.canHaveAsStopTime(null);
        validTimePeriod1.canHaveAsStopTime(LocalDateTime.now());
    }

}
