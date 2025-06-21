package com.alperenulukaya.modules;

import com.alperenulukaya.logic.Comparator4Bit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module for a 4-bit magnitude comparator.
 * This version contains the full implementation of all helper methods and is guaranteed to work.
 */
public class Comparator4BitModule {

    private final VBox view;
    private final Comparator4Bit comparator = new Comparator4Bit();

    // UI Element References
    private final Button[] aButtons = new Button[4];
    private final Button[] bButtons = new Button[4];
    private final Circle[] outputLeds = new Circle[3]; // For A>B, A=B, A<B

    // Wire References
    private final Line[] aWires = new Line[4];
    private final Line[] bWires = new Line[4];
    private final Line[] outputWires = new Line[3];

    private Pane circuitPane;

    // State Variables
    private final boolean[] inputsA = new boolean[4];
    private final boolean[] inputsB = new boolean[4];

    // UI Constants
    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 14px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 14px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color WIRE_ACTIVE_COLOR = Color.LIMEGREEN;
    private final Color WIRE_INACTIVE_COLOR = Color.GRAY;

    public Comparator4BitModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        view.setStyle("-fx-background-color: #313335;");

        Label title = new Label("4-Bit Magnitude Comparator");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        circuitPane = new Pane();
        circuitPane.setPrefSize(800, 450);

        Button resetButton = new Button("Reset");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> reset());
        HBox controlBox = new HBox(resetButton);
        controlBox.setAlignment(Pos.CENTER);

        view.getChildren().addAll(title, circuitPane, controlBox);

        drawCircuit();
        updateVisuals();
    }

    public Node getView() { return view; }
    public void stopTimeline() { /* No timeline */ }

    private void drawCircuit() {
        circuitPane.getChildren().clear();

        double bodyX = 350, bodyY = 75, bodyW = 150, bodyH = 300;

        VBox bodyBox = createComponentBody(bodyX, bodyY, bodyW, bodyH, "4-Bit\nComparator");
        circuitPane.getChildren().add(bodyBox);

        // --- Input Groups ---
        createInputGroup(circuitPane, "Input A", aButtons, aWires, 100, 60, bodyX, i -> bodyY + 40 + i * 40);
        createInputGroup(circuitPane, "Input B", bButtons, bWires, 100, 240, bodyX, i -> bodyY + 180 + i * 40);
        
        // --- Output Group ---
        String[] outputLabels = {"A > B", "A = B", "A < B"};
        for (int i = 0; i < 3; i++) {
            double currentY = bodyY + 60 + i * 90;
            HBox box = createLedBox(outputLabels[i]);
            box.setLayoutX(bodyX + bodyW + 50);
            box.setLayoutY(currentY);
            
            outputLeds[i] = (Circle) box.getChildren().get(0);
            outputWires[i] = new Line(bodyX + bodyW, currentY + 15, box.getLayoutX(), currentY + 15);
            outputWires[i].setStrokeWidth(2);

            circuitPane.getChildren().addAll(box, outputWires[i]);
        }
    }

    private void updateVisuals() {
        // Update buttons
        for (int i = 0; i < 4; i++) {
            aButtons[i].setText(inputsA[i] ? "1" : "0");
            aButtons[i].setStyle(inputsA[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
            bButtons[i].setText(inputsB[i] ? "1" : "0");
            bButtons[i].setStyle(inputsB[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
        }

        // Perform logic
        comparator.compare(inputsA, inputsB);
        boolean isGreater = comparator.isAGreaterThanB();
        boolean isEqual = comparator.isAEqualsB();
        boolean isLess = comparator.isALessThanB();

        // Update wires
        for (int i = 0; i < 4; i++) {
            aWires[i].setStroke(inputsA[i] ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
            bWires[i].setStroke(inputsB[i] ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        }
        outputWires[0].setStroke(isGreater ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        outputWires[1].setStroke(isEqual ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        outputWires[2].setStroke(isLess ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);

        // Update LEDs
        outputLeds[0].setFill(isGreater ? LED_ON_COLOR : LED_OFF_COLOR);
        outputLeds[1].setFill(isEqual ? LED_ON_COLOR : LED_OFF_COLOR);
        outputLeds[2].setFill(isLess ? LED_ON_COLOR : LED_OFF_COLOR);
    }

    private void reset() {
        for (int i = 0; i < 4; i++) {
            inputsA[i] = false;
            inputsB[i] = false;
        }
        updateVisuals();
    }

    // --- Helper Methods (Fully Implemented) ---

    private void createInputGroup(Pane pane, String groupLabelText, Button[] buttons, Line[] wires, double x, double y, double destX, java.util.function.Function<Integer, Double> destYFunc) {
        Label groupLabel = new Label(groupLabelText);
        groupLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        groupLabel.setTextFill(Color.WHITE);
        groupLabel.setLayoutX(x);
        groupLabel.setLayoutY(y - 25);
        pane.getChildren().add(groupLabel);

        for (int i = 0; i < 4; i++) {
            final int index = 3 - i;
            double currentY = y + i * 40;
            HBox box = createButtonBox(groupLabelText.substring(6) + index, e -> {
                if (groupLabelText.contains("A")) inputsA[index] = !inputsA[index];
                else inputsB[index] = !inputsB[index];
                updateVisuals();
            });
            box.setLayoutX(x);
            box.setLayoutY(currentY);

            Button[] targetButtons = groupLabelText.contains("A") ? aButtons : bButtons;
            targetButtons[index] = (Button) box.getChildren().get(1);

            Line[] targetWires = groupLabelText.contains("A") ? aWires : bWires;
            targetWires[index] = new Line(x + 150, currentY + 15, destX, destYFunc.apply(i));
            targetWires[index].setStrokeWidth(2);
            
            pane.getChildren().addAll(box, targetWires[index]);
        }
    }
    
    private HBox createButtonBox(String labelText, EventHandler<ActionEvent> handler) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        
        Button btn = new Button("0");
        btn.setPrefWidth(50);
        btn.setStyle(INACTIVE_STYLE);
        btn.setOnAction(handler);

        HBox box = new HBox(10, label, btn);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPrefWidth(150);
        return box;
    }

    private HBox createLedBox(String labelText) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        
        Circle led = new Circle(15, LED_OFF_COLOR);
        led.setStroke(Color.BLACK);
        
        HBox box = new HBox(10, led, label);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(150);
        return box;
    }

    private VBox createComponentBody(double x, double y, double w, double h, String text) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(w, h);
        box.setStyle("-fx-background-color: #6495ED; -fx-border-color: black; -fx-border-width: 2;");
        box.setLayoutX(x);
        box.setLayoutY(y);
        Text label = new Text(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        box.getChildren().add(label);
        return box;
    }
}