package be.kuleuven.cs.swop;


public class App {

    /**
     * 
     * @param args
     *            | 0: initialisation file.
     */
    public static void main(String[] args) {
        UserInterface ui = new CLI();
        FacadeController facade;
        if (args.length == 1) {
            String initialisationFilePath = args[0];
            facade = new FacadeController(initialisationFilePath);
        } else {
            facade = new FacadeController();
        }
        new SessionController(ui, facade);
        ui.start();
    }
}
