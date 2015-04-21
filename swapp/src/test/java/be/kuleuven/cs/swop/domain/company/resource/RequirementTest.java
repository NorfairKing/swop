package be.kuleuven.cs.swop.domain.company.resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class RequirementTest {

    @Test
    public void satisfiedTest () {
        ResourceType goodType = new ResourceType("res", new HashSet<>(), new HashSet<>(), false);

        Requirement smallReq = new Requirement(1, goodType);
        Requirement bigReq = new Requirement(2, goodType);

        assertFalse(smallReq.isSatisfiedWith(new HashSet<>()));
        assertFalse(bigReq.isSatisfiedWith(new HashSet<>()));
        
        Resource goodRes = new Resource(goodType, "grommel");
        Set<Resource> smallSet = new HashSet<>(Arrays.asList(goodRes));

        assertTrue(smallReq.isSatisfiedWith(smallSet));
        assertFalse(bigReq.isSatisfiedWith(smallSet));
        
        ResourceType uselessType = new ResourceType("different res", new HashSet<>(), new HashSet<>(), false);
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
