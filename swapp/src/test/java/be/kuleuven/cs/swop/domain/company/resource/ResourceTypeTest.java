package be.kuleuven.cs.swop.domain.company.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
