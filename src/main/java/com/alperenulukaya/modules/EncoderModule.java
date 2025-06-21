package com.alperenulukaya.modules;

import com.alperenulukaya.logic.Encoder8to3;

import javafx.application.Platform;
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

public class EncoderModule {

    private final VBox view;
    private final Encoder8to3 encoder = new Encoder8to3();

    private final Button[] inputButtons = new Button[8];
    private final Circle[] outputLeds = new Circle[3];
    private Button enableButton;
    private Pane circuitPane;
    private int activeInput = -1;
    private boolean enableInput = true;

    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color WIRE_ACTIVE_1_COLOR = Color.LIMEGREEN;
    private final Color WIRE_ACTIVE_0_COLOR = Color.CYAN;
    private final Color WIRE_INACTIVE_COLOR = Color.GRAY;
    private final Color WIRE_ENABLED_IDLE_COLOR = Color.DODGERBLUE;

    public EncoderModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("8-to-3 Line Encoder with Enable");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        circuitPane = new Pane();
        circuitPane.setPrefSize(600, 450);

        Button resetButton = new Button("Reset");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> {
            activeInput = -1;
            enableInput = true;
            updateVisuals();
        });

        view.getChildren().addAll(title, circuitPane, resetButton);

        Platform.runLater(() -> {
            drawCircuit();
            updateVisuals();
        });
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        /* No timeline */ }

    private void drawCircuit() {
        circuitPane.getChildren().clear();

        double encoderStartX = 200;
        double startY = 50;
        double bodyWidth = 150;
        double bodyHeight = 350;

        VBox encoderBox = createComponentBody(encoderStartX, startY, bodyWidth, bodyHeight, "8-to-3\nEncoder");
        circuitPane.getChildren().add(encoderBox);

        for (int i = 0; i < 8; i++) {
            final int index = i;
            double yPos = startY + 25 + i * 40;

            Button btn = createToggleButton("Press", 80, e -> {
                activeInput = (activeInput == index) ? -1 : index;
                updateVisuals();
            });
            inputButtons[i] = btn;

            HBox inputBox = createLabeledComponent(new Label("I" + i), btn);
            inputBox.setLayoutX(50);
            inputBox.setLayoutY(yPos - 12);
            circuitPane.getChildren().add(inputBox);
        }

        for (int i = 0; i < 3; i++) {
            double yPos = startY + 60 + i * 80;

            outputLeds[i] = new Circle(15, LED_OFF_COLOR);
            outputLeds[i].setStroke(Color.BLACK);

            HBox outputBox = createLabeledComponent(outputLeds[i], new Label("A" + (2 - i)));
            outputBox.setLayoutX(encoderStartX + bodyWidth + 20);
            outputBox.setLayoutY(yPos - 12);
            circuitPane.getChildren().add(outputBox);
        }

        enableButton = createToggleButton("1", 50, e -> {
            enableInput = !enableInput;
            updateVisuals();
        });

        HBox enableBox = createLabeledComponent(new Label("E"), enableButton);
        enableBox.setLayoutX(encoderStartX + (bodyWidth / 2) - 45);
        enableBox.setLayoutY(startY - 40);
        circuitPane.getChildren().add(enableBox);
    }

    private void drawWires() {
        circuitPane.getChildren().removeIf(node -> node instanceof Line);

        double encoderStartX = 200;
        double encoderStartY = 50;
        double encoderBodyWidth = 150;

        // Input Wires
        for (int i = 0; i < 8; i++) {
            double yPos = encoderStartY + 25 + i * 40;

            // FINAL FIX: Use a fixed X-coordinate for the wire's start point.
            // This avoids the timing issue where the HBox width is not yet calculated on launch.
            // The value 170 is a reliable position just to the right of the input buttons.
            double wireStartX = 170;

            Line wire = new Line(wireStartX, yPos, encoderStartX, yPos);
            wire.setStrokeWidth(3);
            wire.setStroke(i == activeInput ? WIRE_ACTIVE_1_COLOR : WIRE_INACTIVE_COLOR);
            circuitPane.getChildren().add(wire);
        }

        // Output Wires
        boolean[] outputBits = encoder.getOutput(enableInput, activeInput);
        for (int i = 0; i < 3; i++) {
            double yPos = encoderStartY + 60 + i * 80;
            HBox outputBox = (HBox) outputLeds[i].getParent();
            Line wire = new Line(encoderStartX + encoderBodyWidth, yPos, outputBox.getLayoutX(), yPos);
            wire.setStrokeWidth(3);

            if (enableInput) {
                if (activeInput != -1) {
                    wire.setStroke(outputBits[i] ? WIRE_ACTIVE_1_COLOR : WIRE_ACTIVE_0_COLOR);
                } else {
                    wire.setStroke(WIRE_ENABLED_IDLE_COLOR);
                }
            } else {
                wire.setStroke(WIRE_INACTIVE_COLOR);
            }
            circuitPane.getChildren().add(wire);
        }

        // Enable Wire
        HBox enableBox = (HBox) enableButton.getParent();
        Line enableWire = new Line(enableBox.getLayoutX() + 45, enableBox.getLayoutY() + 15, encoderStartX + (encoderBodyWidth / 2), encoderStartY);
        enableWire.setStrokeWidth(3);
        enableWire.setStroke(enableInput ? WIRE_ACTIVE_1_COLOR : WIRE_INACTIVE_COLOR);
        circuitPane.getChildren().add(enableWire);
    }

    private void updateVisuals() {
        for (int i = 0; i < 8; i++) {
            if (i == activeInput) {
                inputButtons[i].setStyle(ACTIVE_STYLE);
                inputButtons[i].setText("Active");
            } else {
                inputButtons[i].setStyle(INACTIVE_STYLE);
                inputButtons[i].setText("Press");
            }
        }

        enableButton.setText(enableInput ? "1" : "0");
        enableButton.setStyle(enableInput ? ACTIVE_STYLE : INACTIVE_STYLE);

        boolean[] output = encoder.getOutput(enableInput, activeInput);
        for (int i = 0; i < 3; i++) {
            outputLeds[i].setFill(output[i] ? LED_ON_COLOR : LED_OFF_COLOR);
        }

        drawWires();
    }

    // --- Helper Methods for UI Creation ---
    private VBox createComponentBody(double x, double y, double w, double h, String text) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(w, h);
        box.setStyle("-fx-background-color: #6495ED; -fx-border-color: black;");
        box.setLayoutX(x);
        box.setLayoutY(y);
        Text label = new Text(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        box.getChildren().add(label);
        return box;
    }

    private Button createToggleButton(String text, double width, EventHandler<ActionEvent> handler) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setStyle(INACTIVE_STYLE);
        btn.setOnAction(handler);
        return btn;
    }

    private HBox createLabeledComponent(Node control, Label label) {
        label.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        HBox box = new HBox(10, control, label);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private HBox createLabeledComponent(Label label, Button control) {
        label.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        HBox box = new HBox(10, label, control);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }
}
