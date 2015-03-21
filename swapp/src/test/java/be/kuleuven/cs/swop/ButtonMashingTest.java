package be.kuleuven.cs.swop;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ButtonMashingTest {

    private static ButtonMashingUI  ui;
    private static FacadeController facade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        ui = new ButtonMashingUI();
        facade = new FacadeController();
        new SessionController(ui, facade);
        ui.start();
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void test() {
        int amount = 100000; // arbitrary number
        ui.performActions(amount);
    }

}
