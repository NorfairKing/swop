package be.kuleuven.cs.swop;


import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskMan;


public class App {

    /**
     * 
     * @param args
     *            | 0: initialisation file.
     */
    public static void main(String[] args) {
        UserInterface ui = new CLI();
        TaskMan taskMan = new TaskMan();
        if (args.length == 1) {
            String filePath = args[0];
            YAMLReader reader = new YAMLReader(taskMan);
            if(reader.read(filePath)){
    			System.out.println("Successfully imported file.");
            }
        }
        new SessionController(ui, taskMan);
        ui.start();
    }
}
