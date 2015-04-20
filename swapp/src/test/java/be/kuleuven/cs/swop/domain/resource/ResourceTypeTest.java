package be.kuleuven.cs.swop.domain.resource;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ResourceTypeTest {
    
    private ResourceType type1;
    private ResourceType type2;
    private ResourceType type3;
    private ResourceType type4;

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
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
