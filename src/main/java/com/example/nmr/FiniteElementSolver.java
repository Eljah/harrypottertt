package com.example.nmr;

/**
 * Simple 2D finite element solver for magnetic potential.
 */
public class FiniteElementSolver {
    private final int width;
    private final int height;
    private final double[][] potential;
    private final boolean[][] fixed;

    public FiniteElementSolver(int width, int height) {
        this.width = width;
        this.height = height;
        this.potential = new double[height][width];
        this.fixed = new boolean[height][width];
    }

    /**
     * Defines a rectangular coil region with a fixed potential value.
     */
    public void setCoil(int x1, int y1, int x2, int y2, double value) {
        for (int y = y1; y < y2 && y < height; y++) {
            for (int x = x1; x < x2 && x < width; x++) {
                potential[y][x] = value;
                fixed[y][x] = true;
            }
        }
    }

    /**
     * Performs Gauss-Seidel iterations until convergence or maxIter reached.
     * @return true if converged
     */
    public boolean solveUntilConverged(int maxIter, double tolerance) {
        for (int iter = 0; iter < maxIter; iter++) {
            double maxDelta = 0.0;
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    if (fixed[y][x]) continue;
                    double newVal = 0.25 * (potential[y-1][x] + potential[y+1][x] +
                                            potential[y][x-1] + potential[y][x+1]);
                    maxDelta = Math.max(maxDelta, Math.abs(newVal - potential[y][x]));
                    potential[y][x] = newVal;
                }
            }
            if (maxDelta < tolerance) {
                return true;
            }
        }
        return false;
    }

    public double getPotential(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }
        return potential[y][x];
    }
}
