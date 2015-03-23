package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.facade.FacadeController;
import be.kuleuven.cs.swop.facade.SessionController;


public class App {

    /**
     * 
     * @param args
     *            | 0: initialisation file.
     */
    public static void main(String[] args) {
        UserInterface ui = new CLI();
        FacadeController facade = new FacadeController();
        if (args.length == 1) {
            String filePath = args[0];
            YAMLReader reader = new YAMLReader(facade);
            reader.read(filePath);
        }
        new SessionController(ui, facade);
        ui.start();
    }
}
