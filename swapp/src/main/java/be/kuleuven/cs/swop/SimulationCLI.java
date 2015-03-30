package be.kuleuven.cs.swop;


public class SimulationCLI extends CLI {

    public boolean start() {
        super.start();
        boolean wasSuccessful = promptSuccessful();
        return wasSuccessful;
    }

    private boolean promptSuccessful() {
        System.out.println("Was the simulation successful? [y/n]");
        String line = getScanner().nextLine();
        if ("y".equals(line)) {
            return true;
        }
        else if ("n".equals(line)) {
            return false;
        } else {
            System.out.println("Invalid input, try again.");
            return promptSuccessful();
        }
    }

}
