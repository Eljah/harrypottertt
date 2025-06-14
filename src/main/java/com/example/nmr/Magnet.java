package com.example.nmr;

/**
 * Represents parameters of the permanent electromagnet for the NMR mouse.
 */
public class Magnet {
    private final double coilTurns;    // Number of turns
    private final double coilCurrent;  // Current in amperes
    private final double coreArea;     // Cross-sectional area in m^2
    private final double relativePermeability;

    public Magnet(double coilTurns, double coilCurrent, double coreArea, double relativePermeability) {
        this.coilTurns = coilTurns;
        this.coilCurrent = coilCurrent;
        this.coreArea = coreArea;
        this.relativePermeability = relativePermeability;
    }

    /**
     * Calculates magnetic field B (Tesla) produced by the magnet using a simple formula.
     */
    public double calculateField() {
        double mu0 = 4e-7 * Math.PI; // vacuum permeability
        return mu0 * relativePermeability * coilTurns * coilCurrent / coreArea;
    }
}
