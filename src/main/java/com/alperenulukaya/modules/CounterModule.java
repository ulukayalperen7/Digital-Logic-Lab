package com.alperenulukaya.modules;

import com.alperenulukaya.logic.Counter4Bit;
import com.alperenulukaya.logic.DisplayDriver;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * A self-contained module for the 4-Bit Up/Down Counter simulation. It handles
 * its own UI, logic, and animation.
 */
public class CounterModule {

    private VBox view; // The main UI node for this module

    // --- Core Simulation Objects ---
    private final Counter4Bit counter = new Counter4Bit();
    private final DisplayDriver driver = new DisplayDriver();

    // --- UI Elements ---
    private final Circle[] leds = new Circle[4];
    private final Rectangle[] segments = new Rectangle[7];
    private final Line[] circuitLines = new Line[3];
    private Label binaryLabel, decimalLabel, hexLabel;
    private Button modeButton, autoClockButton;

    // --- Animation and State ---
    private Timeline autoClockTimeline;
    private boolean isAutoClockRunning = false;

    // --- UI Constants ---
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color SEGMENT_ON_COLOR = Color.RED;
    private final Color SEGMENT_OFF_COLOR = Color.rgb(40, 40, 40, 0.8);
    private final Color LINE_ON_COLOR = Color.ORANGE;
    private final Color LINE_OFF_COLOR = Color.GRAY;

    public CounterModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = createTitleArea();
        VBox displayArea = createDisplayArea();
        VBox controlArea = createControlArea();

        view.getChildren().addAll(title, displayArea, controlArea);

        setupAutoClock(1.0);
        updateUI();
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        if (autoClockTimeline != null) {
            autoClockTimeline.stop();
        }
    }

    // --- UI Creation Methods ---
    private Label createTitleArea() {
        Label title = new Label("4-Bit Up/Down Counter Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));
        return title;
    }

    private VBox createDisplayArea() {
        HBox ledBox = createLedDisplay();
        VBox sevenSegmentDisplay = createSevenSegmentDisplay();
        Pane circuitDiagram = createCircuitDiagram();
        HBox infoLabels = createInfoLabels();

        VBox centerVBox = new VBox(40, ledBox, sevenSegmentDisplay, circuitDiagram, infoLabels);
        centerVBox.setAlignment(Pos.CENTER);
        return centerVBox;
    }

    private HBox createLedDisplay() {
        HBox ledBox = new HBox(25);
        ledBox.setAlignment(Pos.CENTER);
        for (int i = 3; i >= 0; i--) {
            leds[i] = new Circle(25, LED_OFF_COLOR);
            leds[i].setStroke(Color.BLACK);
            Label bitLabel = new Label("Q" + i);
            bitLabel.setTextFill(Color.WHITE);
            bitLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
            VBox ledContainer = new VBox(10, bitLabel, leds[i]);
            ledContainer.setAlignment(Pos.CENTER);
            ledBox.getChildren().add(ledContainer);
        }
        return ledBox;
    }

    private VBox createSevenSegmentDisplay() {
        final double segWidth = 80, segHeight = 15;
        Pane displayPane = new Pane();
        displayPane.setPrefSize(segWidth + 2 * segHeight + 10, 2 * segWidth + 3 * segHeight + 10);

        double[][] positions = {
            {segHeight + 5, 5},
            {segWidth + segHeight + 5, segHeight + 5},
            {segWidth + segHeight + 5, segWidth + 2 * segHeight + 5},
            {segHeight + 5, 2 * segWidth + 2 * segHeight + 5},
            {5, segWidth + 2 * segHeight + 5},
            {5, segHeight + 5},
            {segHeight + 5, segWidth + segHeight + 5}
        };

        for (int i = 0; i < 7; i++) {
            boolean isHorizontal = (i == 0 || i == 3 || i == 6);
            segments[i] = new Rectangle(isHorizontal ? segWidth : segHeight, isHorizontal ? segHeight : segWidth);
            segments[i].setFill(SEGMENT_OFF_COLOR);
            segments[i].setArcWidth(10);
            segments[i].setArcHeight(10);
            segments[i].relocate(positions[i][0], positions[i][1]);
            displayPane.getChildren().add(segments[i]);
        }

        VBox container = new VBox(displayPane);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private Pane createCircuitDiagram() {
        Pane pane = new Pane();
        pane.setPrefHeight(100);

        double startX = 50;
        double spacing = 180;
        for (int i = 0; i < 4; i++) {
            Rectangle rect = new Rectangle(startX + i * spacing, 20, 60, 40);
            rect.setFill(Color.CORNFLOWERBLUE);
            rect.setStroke(Color.BLACK);
            Label ffLabel = new Label("T-FF " + i);
            ffLabel.relocate(startX + i * spacing + 10, 30);
            pane.getChildren().addAll(rect, ffLabel);
        }

        circuitLines[0] = new Line(startX + 60, 40, startX + spacing, 40);
        circuitLines[1] = new Line(startX + spacing + 60, 40, startX + 2 * spacing, 40);
        circuitLines[2] = new Line(startX + 2 * spacing + 60, 40, startX + 3 * spacing, 40);

        for (Line line : circuitLines) {
            line.setStrokeWidth(3);
            pane.getChildren().add(line);
        }
        return pane;
    }

    private HBox createInfoLabels() {
        binaryLabel = new Label();
        decimalLabel = new Label();
        hexLabel = new Label();
        Font labelFont = Font.font("Consolas", FontWeight.BOLD, 24);

        binaryLabel.setFont(labelFont);
        decimalLabel.setFont(labelFont);
        hexLabel.setFont(labelFont);
        binaryLabel.setTextFill(Color.WHITE);
        decimalLabel.setTextFill(Color.WHITE);
        hexLabel.setTextFill(Color.WHITE);

        HBox textLabels = new HBox(40, binaryLabel, decimalLabel, hexLabel);
        textLabels.setAlignment(Pos.CENTER);
        return textLabels;
    }

    private VBox createControlArea() {
        Button clockButton = new Button("Manual Clock");
        Button resetButton = new Button("Reset");
        modeButton = new Button("Mode: UP");
        autoClockButton = new Button("Start Auto-Clock");

        Font buttonFont = Font.font(16);
        clockButton.setFont(buttonFont);
        resetButton.setFont(buttonFont);
        modeButton.setFont(buttonFont);
        autoClockButton.setFont(buttonFont);

        clockButton.setOnAction(e -> handleClock());
        resetButton.setOnAction(e -> handleReset());
        modeButton.setOnAction(e -> handleModeChange());
        autoClockButton.setOnAction(e -> handleAutoClock());

        HBox topControlRow = new HBox(30, clockButton, resetButton, modeButton);
        topControlRow.setAlignment(Pos.CENTER);

        Label speedLabel = new Label("Speed (Hz):");
        speedLabel.setTextFill(Color.WHITE);
        Slider speedSlider = new Slider(0.5, 10, 1);
        speedSlider.setPrefWidth(200);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(2.5);
        speedSlider.setBlockIncrement(0.5);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            setupAutoClock(newVal.doubleValue());
            if (isAutoClockRunning) {
                autoClockTimeline.play();
            }
        });

        HBox bottomControlRow = new HBox(15, autoClockButton, speedLabel, speedSlider);
        bottomControlRow.setAlignment(Pos.CENTER);

        VBox controlBox = new VBox(20, topControlRow, bottomControlRow);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(30, 0, 0, 0));
        return controlBox;
    }

    // --- Event Handlers and Logic ---
    private void setupAutoClock(double hertz) {
        if (autoClockTimeline != null) {
            autoClockTimeline.stop();
        }
        double cycleDuration = 1000.0 / hertz; // in milliseconds
        autoClockTimeline = new Timeline(new KeyFrame(Duration.millis(cycleDuration), e -> handleClock()));
        autoClockTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void handleClock() {
        counter.clock();
        updateUI();
    }

    private void handleReset() {
        counter.reset();
        updateUI();
    }

    private void handleModeChange() {
        if (counter.getMode() == Counter4Bit.CountMode.UP) {
            counter.setMode(Counter4Bit.CountMode.DOWN);
            modeButton.setText("Mode: DOWN");
        } else {
            counter.setMode(Counter4Bit.CountMode.UP);
            modeButton.setText("Mode: UP");
        }
        updateUI();
    }

    private void handleAutoClock() {
        isAutoClockRunning = !isAutoClockRunning;
        if (isAutoClockRunning) {
            autoClockButton.setText("Stop Auto-Clock");
            autoClockTimeline.play();
        } else {
            autoClockButton.setText("Start Auto-Clock");
            autoClockTimeline.stop();
        }
    }

    private void updateUI() {
        int decimalValue = counter.getValue();
        boolean[] bits = counter.getBits();
        boolean[] segmentStates = driver.getSegmentsFor(decimalValue);

        for (int i = 0; i < 4; i++) {
            leds[i].setFill(bits[i] ? LED_ON_COLOR : LED_OFF_COLOR);
        }

        for (int i = 0; i < 7; i++) {
            segments[i].setFill(segmentStates[i] ? SEGMENT_ON_COLOR : SEGMENT_OFF_COLOR);
        }

        // Update circuit diagram lines
        boolean q0 = bits[0];
        boolean q1 = bits[1];
        circuitLines[0].setStroke(q0 ? LINE_ON_COLOR : LINE_OFF_COLOR);
        circuitLines[1].setStroke(q0 && q1 ? LINE_ON_COLOR : LINE_OFF_COLOR);
        circuitLines[2].setStroke(q0 && q1 && bits[2] ? LINE_ON_COLOR : LINE_OFF_COLOR);

        binaryLabel.setText(String.format("Binary: %d%d%d%d", bits[3] ? 1 : 0, bits[2] ? 1 : 0, bits[1] ? 1 : 0, bits[0] ? 1 : 0));
        decimalLabel.setText("Decimal: " + decimalValue);
        hexLabel.setText("Hex: " + Integer.toHexString(decimalValue).toUpperCase());
    }
}
