package be.kuleuven.cs.swop;


public class App {

    /**
     * 
     * @param args
     *        | 0: initialisation file.
     */
	public static void main(String[] args) {
	    if (args.length != 1) throw new IllegalArgumentException("Not enough or too many arguments.");
	    
		CLI ui = new CLI();
        
		String initialisationFilePath = args[0];
        FacadeController facade = new FacadeController(initialisationFilePath);
        
        new SessionController(ui,facade);
        
		ui.start();
	}
}
