package be.kuleuven.cs.swop.domain.company.resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RequirementsTest {

    // These come from Task
    @Test
    public void requirementTests() {
        Task task = new Task("task", 10, 1, null);
        assertEquals(0, task.getRequirements().size());

        Task task2 = new Task("task", 10, 1,
                new HashSet<>());
        assertEquals(0, task2.getRequirements().size());

        assertEquals(0, task2.getRecursiveRequirements().size());
    }

    @Test
    public void recursiveRequirementTests() {
        Task task0 = new Task("task", 10, 1,
                new HashSet<>());

        assertEquals(0, task0.getRecursiveRequirements().size());

        Task task1 = new Task("task", 10, 1,
                new HashSet<>(Arrays.asList(
                        new Requirement(1,
                                new ResourceType("res", new HashSet<>(), new HashSet<>(), false)
                        )
                        )
                ));
        assertEquals(1, task1.getRecursiveRequirements().size());

        ResourceType dependOn = new ResourceType("res", new HashSet<>(), new HashSet<>(), false);
        ResourceType depender = new ResourceType("res",
                new HashSet<>(Arrays.asList(dependOn)), new HashSet<>(), false);
        Task task2 = new Task("task", 10, 1,
                new HashSet<>(Arrays.asList(new Requirement(1, depender))
                ));
        assertEquals(2, task2.getRecursiveRequirements().size());
    }
}
