package com.example.nmr;

import java.util.Arrays;

/**
 * Simulates NMR signals (T1/T2) for a large NMR mouse applied to asphalt.
 */
public class NmrSignalSimulator {
    // Basic simulation parameters
    private final Magnet magnet;
    private double waterContent;  // fraction 0..1

    public NmrSignalSimulator(Magnet magnet, double waterContent) {
        this.magnet = magnet;
        this.waterContent = waterContent;
    }

    /**
     * Calculates resonance frequency for protons based on the magnet field.
     */
    public double getLarmorFrequency() {
        double gamma = 42.58e6; // Hz/T for protons
        return gamma * magnet.calculateField();
    }

    /**
     * Returns simulated T1 relaxation time (ms) based on water content.
     */
    public double getT1() {
        return 100 + 400 * waterContent;
    }

    /**
     * Returns simulated T2 relaxation time (ms) based on water content.
     */
    public double getT2() {
        return 50 + 200 * waterContent;
    }

    /**
     * Generates a synthetic NMR free induction decay signal.
     */
    public double[] generateSignal(int points, double dtMs) {
        double t1 = getT1();
        double t2 = getT2();
        double freq = getLarmorFrequency();
        double[] result = new double[points];
        for (int i = 0; i < points; i++) {
            double t = i * dtMs; // time in ms
            // Simple exponential decay with T2 and oscillation with freq
            double envelope = Math.exp(-t / t2);
            result[i] = envelope * Math.cos(2 * Math.PI * freq * t / 1000.0);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Field=" + magnet.calculateField() + " T, " +
               "T1=" + getT1() + " ms, T2=" + getT2() + " ms";
    }
}
