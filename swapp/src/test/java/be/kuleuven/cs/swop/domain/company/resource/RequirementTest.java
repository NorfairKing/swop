package be.kuleuven.cs.swop.domain.company.resource;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class RequirementTest {

    private static ResourceType goodType;
    private static ResourceType uselessType;

    @Before
    public void setUp() throws Exception {
        goodType = new ResourceType("res", new HashSet<>(), new HashSet<>(), false);
        uselessType = new ResourceType("different res", new HashSet<>(), new HashSet<>(), false);
    }

    @Test
    public void constructorValidTest1() {
        new Requirement(5, goodType);
    }

    @Test
    public void constructorValidTest2() {
        new Requirement(1, uselessType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAmountTest1() {
        new Requirement(0, goodType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAmountTest2() {
        new Requirement(-1, goodType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidTypeTest() {
        new Requirement(2, null);
    }

    @Test
    public void satisfiedTest() {

        Requirement smallReq = new Requirement(1, goodType);
        Requirement bigReq = new Requirement(2, goodType);

        assertFalse(smallReq.isSatisfiedWith(new HashSet<>()));
        assertFalse(bigReq.isSatisfiedWith(new HashSet<>()));

        Resource goodRes = new Resource(goodType, "grommel");
        Set<Resource> smallSet = new HashSet<>(Arrays.asList(goodRes));

        assertTrue(smallReq.isSatisfiedWith(smallSet));
        assertFalse(bigReq.isSatisfiedWith(smallSet));

        Resource uselessRes = new Resource(uselessType, "brommel");
        Set<Resource> uselessSet = new HashSet<>(Arrays.asList(uselessRes));

        assertFalse(smallReq.isSatisfiedWith(uselessSet));
        assertFalse(bigReq.isSatisfiedWith(uselessSet));

        Set<Resource> semiUsefulSet = new HashSet<>(Arrays.asList(goodRes, uselessRes));
        assertTrue(smallReq.isSatisfiedWith(semiUsefulSet));
        assertFalse(bigReq.isSatisfiedWith(semiUsefulSet));

        Resource secondGoodRes = new Resource(goodType, "grommel");
        Set<Resource> bigSet = new HashSet<>(Arrays.asList(goodRes, secondGoodRes));

        assertTrue(smallReq.isSatisfiedWith(bigSet));
        assertTrue(bigReq.isSatisfiedWith(bigSet));
    }

}
