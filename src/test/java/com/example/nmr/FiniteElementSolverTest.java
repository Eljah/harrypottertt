package com.example.nmr;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Tests for the FiniteElementSolver.
 */
public class FiniteElementSolverTest {
    @Test
    public void coilConverges() {
        FiniteElementSolver solver = new FiniteElementSolver(20, 20);
        solver.setCoil(8, 8, 12, 12, 1.0);
        boolean converged = solver.solveUntilConverged(5000, 1e-5);
        assertTrue(converged, "Solver should converge for given coil size.");

        double center = solver.getPotential(10, 10);
        assertFalse(Double.isNaN(center), "Potential must be valid");

        Properties props = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/solver.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            fail("Unable to load expected properties: " + e.getMessage());
        }
        String expectedStr = props.getProperty("expectedPotential");
        if (expectedStr != null) {
            double expected = Double.parseDouble(expectedStr);
            assertEquals(expected, center, 1e-6, "Potential should match expected value");
        }
    }
}
