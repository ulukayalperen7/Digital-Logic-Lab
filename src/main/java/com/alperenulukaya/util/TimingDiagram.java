
package com.alperenulukaya.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * A reusable utility class for drawing timing diagrams on a JavaFX Canvas.
 */
public class TimingDiagram {

    private Canvas canvas;
    private GraphicsContext gc;
    private Map<String, List<Boolean>> signalHistory = new LinkedHashMap<>();
    
    // --- Diagram Constants ---
    private final double PADDING = 20;
    private final double SIGNAL_HEIGHT = 40;
    private final double LABEL_WIDTH = 60;
    private final double TIME_STEP_WIDTH = 25;

    public TimingDiagram(double width, String... signalNames) {
        double height = (signalNames.length * SIGNAL_HEIGHT) + (2 * PADDING);
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        
        for (String name : signalNames) {
            signalHistory.put(name, new ArrayList<>());
        }
        draw();
    }
    
    public Canvas getCanvas() {
        return this.canvas;
    }

    /**
     * Adds a new state for all signals at the current time step.
     * @param states A map of signal names to their current boolean value.
     */
    public void addState(Map<String, Boolean> states) {
        for (Map.Entry<String, Boolean> entry : states.entrySet()) {
            if (signalHistory.containsKey(entry.getKey())) {
                signalHistory.get(entry.getKey()).add(entry.getValue());
            }
        }
        draw();
    }
    
    /**
     * Resets the history of all signals and redraws the empty diagram.
     */
    public void clear() {
        for(List<Boolean> history : signalHistory.values()) {
            history.clear();
        }
        draw();
    }

    /**
     * The main drawing method. It redraws the entire diagram from scratch.
     */
    private void draw() {
        // Clear canvas
        gc.setFill(Color.rgb(43, 43, 43)); // Dark background
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw grid and labels
        drawGridAndLabels();

        // Draw waveforms for each signal
        int signalIndex = 0;
        for (Map.Entry<String, List<Boolean>> entry : signalHistory.entrySet()) {
            drawWaveform(entry.getValue(), signalIndex);
            signalIndex++;
        }
    }

    private void drawGridAndLabels() {
        gc.setStroke(Color.rgb(80, 80, 80)); // Grid color
        gc.setLineWidth(1);
        gc.setFont(Font.font("Consolas", 14));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFill(Color.WHITE);

        int signalIndex = 0;
        for (String signalName : signalHistory.keySet()) {
            double y = PADDING + (signalIndex * SIGNAL_HEIGHT) + (SIGNAL_HEIGHT / 2);
            
            // Draw signal name label
            gc.fillText(signalName, LABEL_WIDTH - 10, y + 5);

            // Draw horizontal line for the signal
            gc.strokeLine(LABEL_WIDTH, y, canvas.getWidth() - PADDING, y);
            
            signalIndex++;
        }
    }

    private void drawWaveform(List<Boolean> history, int signalIndex) {
        if (history.isEmpty()) return;

        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(2.5);

        double startY = PADDING + (signalIndex * SIGNAL_HEIGHT);
        double highY = startY + 5;
        double lowY = startY + SIGNAL_HEIGHT - 5;

        for (int t = 0; t < history.size(); t++) {
            double x1 = LABEL_WIDTH + (t * TIME_STEP_WIDTH);
            double x2 = x1 + TIME_STEP_WIDTH;
            double y1 = history.get(t) ? highY : lowY;

            // Draw horizontal line for this time step
            gc.strokeLine(x1, y1, x2, y1);
            
            // If the state changes from the previous one, draw a vertical line
            if (t > 0 && history.get(t) != history.get(t - 1)) {
                double prevY = history.get(t - 1) ? highY : lowY;
                gc.strokeLine(x1, prevY, x1, y1);
            }
        }
    }
}