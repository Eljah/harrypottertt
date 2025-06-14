package com.example.nmr;

/**
 * Entry point for the NMR mouse simulation.
 */
public class App {
    public static void main(String[] args) {
        // Magnet parameters: turns, current (A), area (m^2), relative permeability
        Magnet magnet = new Magnet(500, 2.0, 0.01, 1000);
        NmrSignalSimulator simulator = new NmrSignalSimulator(magnet, 0.3); // 30% water

        System.out.println(simulator);

        // Solve a simple magnetic potential field using a finite element grid
        FiniteElementSolver fem = new FiniteElementSolver(20, 20);
        fem.setCoil(8, 0, 12, 2, 1.0); // coil at the top of the domain
        boolean converged = fem.solveUntilConverged(2000, 1e-4);
        if (converged) {
            System.out.println("Magnetic potential at center: " + fem.getPotential(10, 10));
        } else {
            System.out.println("FEM solver did not converge");
        }

        double[] signal = simulator.generateSignal(1024, 0.1);
        NmrSignalViewer.showSignal("NMR Mouse Signal", signal);
    }
}
