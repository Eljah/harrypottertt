package com.example.nmr;

import javax.swing.*;
import java.awt.*;

/**
 * Simple Swing component to display a generated NMR signal.
 */
public class NmrSignalViewer extends JPanel {
    private double[] signal;

    public NmrSignalViewer(double[] signal) {
        this.signal = signal;
        setPreferredSize(new Dimension(600, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (signal == null || signal.length == 0) {
            return;
        }
        int w = getWidth();
        int h = getHeight();
        double max = 0;
        for (double v : signal) {
            max = Math.max(max, Math.abs(v));
        }
        if (max == 0) {
            max = 1;
        }
        int points = signal.length;
        for (int i = 0; i < points - 1; i++) {
            int x1 = (int)((double)i / (points - 1) * w);
            int x2 = (int)((double)(i + 1) / (points - 1) * w);
            int y1 = (int)(h / 2 - signal[i] / max * h / 2);
            int y2 = (int)(h / 2 - signal[i + 1] / max * h / 2);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public static void showSignal(String title, double[] signal) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Running in headless mode. Display disabled.");
            // Print first few samples as a simple representation
            for (int i = 0; i < Math.min(signal.length, 10); i++) {
                System.out.printf("%d: %.5f%n", i, signal[i]);
            }
            return;
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new NmrSignalViewer(signal));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
