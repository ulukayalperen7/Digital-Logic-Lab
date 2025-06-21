package com.alperenulukaya.modules;

import com.alperenulukaya.logic.Adder4Bit;

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
 * A completely redesigned UI module for a 4-bit Ripple-Carry Adder. The
 * implementation is based on a static layout with dynamic property updates.
 */
public class Adder4BitModule {

    private final VBox view;
    private final Adder4Bit adder4Bit = new Adder4Bit();

    // UI Element References
    private final Button[] aButtons = new Button[4];
    private final Button[] bButtons = new Button[4];
    private Button carryInButton;
    private final Circle[] sumLeds = new Circle[4];
    private Circle carryOutLed;

    // Wire References for dynamic color changes
    private final Line[] aWires = new Line[4];
    private final Line[] bWires = new Line[4];
    private Line cInWire;
    private final Line[] sWires = new Line[4];
    private Line cOutWire;

    private Pane circuitPane;

    // State Variables
    private final boolean[] inputsA = new boolean[4];
    private final boolean[] inputsB = new boolean[4];
    private boolean initialCarryIn = false;

    // UI Constants
    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 14px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 14px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color WIRE_ACTIVE_COLOR = Color.LIMEGREEN;
    private final Color WIRE_INACTIVE_COLOR = Color.GRAY;

    public Adder4BitModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        view.setStyle("-fx-background-color: #313335;");

        Label title = new Label("4-Bit Ripple-Carry Adder");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        circuitPane = new Pane();
        circuitPane.setPrefSize(800, 500);

        Button resetButton = new Button("Reset");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> reset());
        HBox controlBox = new HBox(resetButton);
        controlBox.setAlignment(Pos.CENTER);

        view.getChildren().addAll(title, circuitPane, controlBox);

        // Build the circuit layout once
        drawCircuit();
        // Set the initial visual state
        updateVisuals();
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        /* No timeline */ }

    private void drawCircuit() {
        circuitPane.getChildren().clear();

        double bodyX = 350, bodyY = 75, bodyW = 150, bodyH = 350;

        // --- Component Body ---
        VBox bodyBox = createComponentBody(bodyX, bodyY, bodyW, bodyH, "4-Bit\nAdder");
        circuitPane.getChildren().add(bodyBox);

        // --- Input Groups (A, B, C0) ---
        // CORRECTED METHOD CALLS with standard Java syntax
        createInputGroup(circuitPane, "Input A", aButtons, aWires, 100, 60, bodyX, i -> bodyY + 40 + i * 35);
        createInputGroup(circuitPane, "Input B", bButtons, bWires, 100, 240, bodyX, i -> bodyY + 190 + i * 35);
        createSingleInput(circuitPane, "C0", 150, 420, bodyX, bodyY + bodyH);

        // --- Output Groups (S, C4) ---
        createOutputGroup(circuitPane, "S", sumLeds, sWires, 4, bodyX + bodyW, 600, i -> bodyY + 40 + i * 85);
        createSingleOutput(circuitPane, "C4", bodyX + bodyW, bodyY, 600, bodyY - 15);
    }

    private void updateVisuals() {
        for (int i = 0; i < 4; i++) {
            aButtons[i].setText(inputsA[i] ? "1" : "0");
            aButtons[i].setStyle(inputsA[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
            bButtons[i].setText(inputsB[i] ? "1" : "0");
            bButtons[i].setStyle(inputsB[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
        }
        carryInButton.setText(initialCarryIn ? "1" : "0");
        carryInButton.setStyle(initialCarryIn ? ACTIVE_STYLE : INACTIVE_STYLE);

        // --- 2. Perform Logic Calculation ---
        adder4Bit.update(inputsA, inputsB, initialCarryIn);
        boolean[] sum = adder4Bit.getSum();
        boolean carryOut = adder4Bit.getCarryOut();

        // --- 3. Update Wire Colors ---
        for (int i = 0; i < 4; i++) {
            aWires[i].setStroke(inputsA[i] ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
            bWires[i].setStroke(inputsB[i] ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
            sWires[i].setStroke(sum[i] ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        }
        cInWire.setStroke(initialCarryIn ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        cOutWire.setStroke(carryOut ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);

        // --- 4. Update LED Colors ---
        for (int i = 0; i < 4; i++) {
            sumLeds[i].setFill(sum[i] ? LED_ON_COLOR : LED_OFF_COLOR);
        }
        carryOutLed.setFill(carryOut ? LED_ON_COLOR : LED_OFF_COLOR);
    }

    private void reset() {
        for (int i = 0; i < 4; i++) {
            inputsA[i] = false;
            inputsB[i] = false;
        }
        initialCarryIn = false;
        updateVisuals();
    }

    // --- Helper methods for building the static circuit ---
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
                if (groupLabelText.contains("A")) {
                    inputsA[index] = !inputsA[index]; 
                }else {
                    inputsB[index] = !inputsB[index];
                }
                updateVisuals();
            });
            box.setLayoutX(x);
            box.setLayoutY(currentY);

            if (groupLabelText.contains("A")) {
                buttons[index] = (Button) box.getChildren().get(1); 
            }else {
                buttons[index] = (Button) box.getChildren().get(1);
            }

            Line wire = new Line(x + 150, currentY + 15, destX, destYFunc.apply(i));
            wire.setStrokeWidth(2);
            if (groupLabelText.contains("A")) {
                aWires[index] = wire; 
            }else {
                bWires[index] = wire;
            }

            pane.getChildren().addAll(box, wire);
        }
    }

    private void createSingleInput(Pane pane, String labelText, double x, double y, double destX, double destY) {
        HBox box = createButtonBox(labelText, e -> {
            initialCarryIn = !initialCarryIn;
            updateVisuals();
        });
        box.setLayoutX(x);
        box.setLayoutY(y);
        this.carryInButton = (Button) box.getChildren().get(1);

        cInWire = new Line(x + 150, y + 15, destX, destY);
        cInWire.setStrokeWidth(2);
        pane.getChildren().addAll(box, cInWire);
    }

    private void createOutputGroup(Pane pane, String prefix, Circle[] leds, Line[] wires, int count, double startX, double x, java.util.function.Function<Integer, Double> yFunc) {
        for (int i = 0; i < count; i++) {
            final int index = 3 - i;
            double currentY = yFunc.apply(i);
            HBox box = createLedBox(prefix + index);
            box.setLayoutX(x);
            box.setLayoutY(currentY);

            leds[index] = (Circle) box.getChildren().get(0);

            wires[index] = new Line(startX, currentY + 15, x, currentY + 15);
            wires[index].setStrokeWidth(2);

            pane.getChildren().addAll(box, wires[index]);
        }
    }

    private void createSingleOutput(Pane pane, String labelText, double startX, double startY, double x, double y) {
        HBox box = createLedBox(labelText);
        box.setLayoutX(x);
        box.setLayoutY(y);
        this.carryOutLed = (Circle) box.getChildren().get(0);

        cOutWire = new Line(startX, startY, x, y + 15);
        cOutWire.setStrokeWidth(2);
        pane.getChildren().addAll(box, cOutWire);
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
