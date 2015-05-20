package be.kuleuven.cs.swop;


import java.io.FileNotFoundException;

import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskMan;


public class App {

    /**
     * 
     * @param args
     *            | 0: initialisation file.
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        UserInterface ui = new CLI();
        TaskMan taskMan = new TaskMan();

        if (args.length == 1) {
            String filePath = args[0];
            taskMan.loadEverythingFromFile(filePath);
        }

        new SessionController(ui, taskMan);

        taskMan.saveEverythingToFile("../save_files/test.json");

        ui.start();
    }
}
