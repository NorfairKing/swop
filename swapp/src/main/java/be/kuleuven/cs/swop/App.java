package be.kuleuven.cs.swop;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App {

    /**
     * 
     * @param args
     *        | 0: initialisation file.
     */
	public static void main(String[] args) {
	    if (args.length != 1) throw new IllegalArgumentException("Not enough or too many arguments.");
	    
		CLI ui = new CLI();
        FacadeController facade = new FacadeController();
        SessionController sc = new SessionController(ui,facade);
        
        String initialisationFilePath = args[0];
        facade.initialiseWith(initialisationFilePath);

		ui.start();
	}
}
