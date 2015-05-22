package be.kuleuven.cs.swop.domain.company.delegation;

import java.util.HashSet;

import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.task.TaskInfo;


public class DelegationTest {

	@Test(expected = IllegalArgumentException.class)
    public void testSetNullDelegation() {
    	Delegation del = new Delegation(null, null, null);
    	del.setDelegationTask(null);
    }
	
	@Test(expected = IllegalStateException.class)
    public void testSetDelegationTwice() {
    	Delegation del = new Delegation(null, null, null);
    	del.setDelegationTask(new Task(new TaskInfo("desc", 10, 1, new Requirements(), new HashSet<>())));
    	del.setDelegationTask(new Task(new TaskInfo("desc", 10, 1, new Requirements(), new HashSet<>())));
    }

}
