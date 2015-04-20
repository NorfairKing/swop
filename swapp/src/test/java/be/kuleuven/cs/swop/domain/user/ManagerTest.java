package be.kuleuven.cs.swop.domain.user;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ManagerTest {

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
        new Manager("Valid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorInvalidTest() {
        new Manager(null);
    }

}
