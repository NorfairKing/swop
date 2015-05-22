package be.kuleuven.cs.swop;


import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.AuthenticationToken;
import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.facade.BranchOfficeWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskMan;


public class ButtonMashingTest {

    private static ButtonMashingUI ui;
    private static TaskMan         facade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        ui = new ButtonMashingUI();
        facade = new TaskMan();
        new SessionController(ui, facade);
        ui.start();
        BranchOfficeWrapper bo = facade.createBranchOffice("Op den boeren buiten.");
        facade.createBranchOffice("In 't stad.");
        Developer d = facade.createDeveloper("Johan",bo);
        facade.createDeveloper("Gerard",bo);
        facade.createManager("Korneel",bo);
        facade.requestAuthenticationFor(bo, d);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void test() {
        int amount = 2000; // arbitrary number
        ui.performActions(amount);

        for (Entry<String, Integer> kvp : ui.getErrorCount().entrySet()) {
            System.out.print(kvp.getValue());
            System.out.print("\t: ");
            System.out.println(kvp.getKey());
        }
    }

}
