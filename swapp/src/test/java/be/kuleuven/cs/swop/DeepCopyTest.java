package be.kuleuven.cs.swop;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.YAMLReader;
import be.kuleuven.cs.swop.facade.TaskMan;



public class DeepCopyTest {
    
    private static TaskMan original;
    private static final String INIT_FILE = "../assignment/iteration1/input.tman";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        original = new TaskMan();
        YAMLReader r= new YAMLReader(original);
        r.read(INIT_FILE);
    }

    @Test
    public void test() {
        TaskMan before = original;
        TaskMan after = before.getDeepCopy();
        assertFalse(before == after);
    }

}
