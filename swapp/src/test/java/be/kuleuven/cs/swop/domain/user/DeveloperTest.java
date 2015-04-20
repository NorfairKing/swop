package be.kuleuven.cs.swop.domain.user;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.user.Developer;


public class DeveloperTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void ConstructorValidTest() {
        new Developer("Valid");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorInvalidTest(){
        new Developer(null);
    }

}
