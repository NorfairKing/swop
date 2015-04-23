package be.kuleuven.cs.swop.domain.company.resource;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ResourceTest {
    
    ResourceType type1;
    ResourceType type2;
    Resource resource1;
    
    @Before
    public void setUp() throws Exception {
        type1 = new ResourceType("valid1", null, null,false);
        resource1 = new Resource(type1, "valid");
    }


    @Test
    public void constructorValidTest() {
        new Resource(type1,"valid1");
    }
    
    @Test
    public void nameTest(){
    	assertTrue("valid".equals(resource1.getName()));

    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidNameEmptyTest(){
        new Resource(type1, "");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidTypeTest(){
        new Resource(null, "valid");
    }

    
    @Test
    public void isOfTypeTest(){
        assertTrue(resource1.isOfType(type1));
        assertFalse(resource1.isOfType(type2));
    }

}
