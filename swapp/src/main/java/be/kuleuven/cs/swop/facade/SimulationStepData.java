package be.kuleuven.cs.swop.facade;


public class SimulationStepData {

    private boolean continueSim;
    private boolean realizeSim;
    
    /**
     * A simple constructor for the SimulationStepData
     * 
     * @param continueSim Should the simulation continue?
     * @param realizeSim Should the simulation be realized? (Ignored if continue is set to true)
     */
    public SimulationStepData(boolean continueSim, boolean realizeSim) {
        this.continueSim = continueSim;
        this.realizeSim = realizeSim;
    }
    
    public boolean getContinueSimulation() {
        return continueSim;
    }
    
    public boolean getRealizeSimulation() {
        return realizeSim;
    }
    
}
