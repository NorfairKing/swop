package be.kuleuven.cs.swop.domain.company.user;


import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ManagerTest {

    @Test
    public void ConstructorValidTest() {
        new Manager("Valid");
    }
    
    @Test
    public void NameTest(){
    	Developer dev = new Developer("Name With Spaces");
    	assertTrue("Name With Spaces".equals(dev.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ConstructorInvalidTest() {
        new Manager(null);
    }

}
