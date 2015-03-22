package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class YAMLReaderTest {

    private static YAMLReader reader;
    private static FacadeController facade;
    
    @Before
    public void setUp() throws Exception {
        facade = new FacadeController();
        reader = new YAMLReader(facade);
    }

    @Test
    public void readProjectsFromFileTest() {
        reader.read(TEST_FILE_PATH);
        
        Set<ProjectWrapper> projects = facade.getProjects();
        assertEquals(projects.size(), 3);
        
        ProjectWrapper projectX = projects.stream()
                .filter(p -> p.getTitle().equals("project x"))
                .findFirst().get();
        ProjectWrapper projectY = projects.stream()
                .filter(p -> p.getTitle().equals("project y"))
                .findFirst().get();
        ProjectWrapper projectZ = projects.stream()
                .filter(p -> p.getTitle().equals("project z"))
                .findFirst().get();

        assertEquals(projectX.getTasks().size(), 1);
        assertEquals(projectY.getTasks().size(), 4);
        assertEquals(projectZ.getTasks().size(), 2);
    }
    

    static final String TEST_FILE_PATH = "src/test/java/be/kuleuven/cs/swop/defaulttest.tman";
}
