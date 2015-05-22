package be.kuleuven.cs.swop.domain.company;


import static org.junit.Assert.assertEquals;

import org.junit.Before;

import be.kuleuven.cs.swop.TestingUI;
import be.kuleuven.cs.swop.domain.company.user.User;
import be.kuleuven.cs.swop.facade.BranchOfficeWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskMan;


public class SelectUserTest {

    private static TestingUI         ui;
    private static TaskMan           taskMan;
    private static SessionController controller;

    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        ui.start();
    }

    public void loginTest() {
        ui.addFileName("test.json");
        controller.loadFromFile();
        BranchOfficeWrapper office = taskMan.getOffices().stream().findFirst().get();
        ui.addOffice(office);
        User user = taskMan.getUsersFrom(office).stream().findFirst().get();
        ui.addSelectUser(user);
        controller.startSelectUserSession();

        assertEquals(office, new BranchOfficeWrapper(taskMan.getCurrentAuthenticationToken().getOffice()));
        assertEquals(user, taskMan.getCurrentAuthenticationToken().getUser());
    }

}
