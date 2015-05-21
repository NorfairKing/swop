package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.TestingUI;


public class BaseFacadeTest {
    
    protected TestingUI  ui;
    protected TaskMan taskMan;
    protected SessionController controller;
    protected BranchOfficeWrapper office;
    protected UserWrapper user;
    
    protected void simpleSetup() {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        
        logInWithFirstUser();
        
        ui.start();
    }
    
    protected void logInWithFirstUser() {
        ui.addFileName("test.json");
        controller.loadFromFile();
        office = taskMan.getOffices().stream().findFirst().get();
        ui.addOffice(office);
        user = taskMan.getUsersFrom(office).stream().findFirst().get();
        ui.addSelectUser(user);
        controller.startSelectUserSession();
    }

}
