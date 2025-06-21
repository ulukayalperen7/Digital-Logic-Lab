package com.alperenulukaya.modules;

import java.util.Map;

import com.alperenulukaya.logic.ShiftRegister4Bit;
import com.alperenulukaya.util.TimingDiagram;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * A UI module to demonstrate a 4-bit SISO (Serial-In, Serial-Out) Shift
 * Register. Features interactive controls and a real-time timing diagram.
 */
public class ShiftRegisterModule {

    private final VBox view;
    private final ShiftRegister4Bit shiftRegister = new ShiftRegister4Bit();

    // UI Elements
    private final Circle[] leds = new Circle[4];
    private Button dataInButton;
    private TimingDiagram timingDiagram;

    // State Variables
    private boolean dataInputState = false;

    // UI Constants
    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;

    public ShiftRegisterModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("4-Bit Shift Register (SISO)");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox mainDisplay = createMainDisplay();

        timingDiagram = new TimingDiagram(800, "CLK", "D_in", "Q0", "Q1", "Q2", "Q3");

        view.getChildren().addAll(title, mainDisplay, timingDiagram.getCanvas());

        updateVisuals();
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        /* No timeline */ }

    private VBox createMainDisplay() {
        // Data Input Section
        dataInButton = new Button("D_in = 0");
        dataInButton.setPrefWidth(120);
        dataInButton.setOnAction(e -> {
            dataInputState = !dataInputState;
            shiftRegister.setDataInput(dataInputState);
            updateVisuals();
        });

        // Circuit/LEDs Section
        HBox circuitBox = new HBox(10);
        circuitBox.setAlignment(Pos.CENTER);

        circuitBox.getChildren().add(dataInButton);

        for (int i = 0; i < 4; i++) {
            VBox ffBox = createFlipFlopBox(i);
            circuitBox.getChildren().add(ffBox);
            if (i < 3) {
                Line connector = new Line(0, 0, 30, 0);
                connector.setStroke(Color.GRAY);
                connector.setStrokeWidth(3);
                circuitBox.getChildren().add(connector);
            }
        }

        // Clock and Reset Buttons
        Button clockButton = new Button("Clock Pulse");
        clockButton.setPrefWidth(200);
        clockButton.setStyle("-fx-font-size: 18px;");
        clockButton.setOnAction(e -> {
            addTimingState(false); // Low clock state
            shiftRegister.clock();
            addTimingState(true);  // High clock state, showing result
            updateVisuals();
        });

        Button resetButton = new Button("Reset");
        resetButton.setPrefWidth(100);
        resetButton.setStyle("-fx-font-size: 18px;");
        resetButton.setOnAction(e -> {
            shiftRegister.reset();
            dataInputState = false;
            timingDiagram.clear();
            updateVisuals();
        });

        HBox controlBox = new HBox(20, clockButton, resetButton);
        controlBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(30, circuitBox, controlBox);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox createFlipFlopBox(int index) {
        Rectangle body = new Rectangle(80, 100, Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);

        leds[index] = new Circle(20, LED_OFF_COLOR);
        leds[index].setStroke(Color.BLACK);

        Label qLabel = new Label("Q" + index);
        qLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox ffBox = new VBox(10, qLabel, body, leds[index]);
        ffBox.setAlignment(Pos.CENTER);
        return ffBox;
    }

    private void addTimingState(boolean clkState) {
        boolean[] bits = shiftRegister.getBits();
        timingDiagram.addState(Map.of(
                "CLK", clkState,
                "D_in", dataInputState,
                "Q0", bits[0],
                "Q1", bits[1],
                "Q2", bits[2],
                "Q3", bits[3]
        ));
    }

    private void updateVisuals() {
        dataInButton.setText("D_in = " + (dataInputState ? "1" : "0"));
        dataInButton.setStyle(dataInputState ? ACTIVE_STYLE : INACTIVE_STYLE);

        boolean[] bits = shiftRegister.getBits();
        for (int i = 0; i < 4; i++) {
            leds[i].setFill(bits[i] ? LED_ON_COLOR : LED_OFF_COLOR);
        }
    }
}
