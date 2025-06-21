
package com.alperenulukaya.modules;

import com.alperenulukaya.logic.Decoder3to8;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module to demonstrate a 3-to-8 line Decoder with an Enable input and
 * connection wires.
 */
public class DecoderModule {

    private final VBox view;
    private final Decoder3to8 decoder = new Decoder3to8();

    // UI Elements
    private final Button[] inputButtons = new Button[3];
    private final Circle[] outputLeds = new Circle[8];
    private Button enableButton;
    private Pane circuitPane;

    // State Variables
    private final boolean[] inputs = new boolean[3];
    private boolean enableInput = true; // Enabled by default

    // UI Constants
    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color WIRE_COLOR = Color.GRAY;

    public DecoderModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("3-to-8 Line Decoder with Enable");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        circuitPane = new Pane();
        circuitPane.setPrefSize(600, 450);

        view.getChildren().addAll(title, circuitPane);

        drawCircuit();
        updateVisuals();
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        /* No timeline */ }

    private void drawCircuit() {
        circuitPane.getChildren().clear();

        double startX = 200;
        double startY = 50;
        double bodyWidth = 150;
        double bodyHeight = 350;

        // Decoder Body
        Rectangle body = new Rectangle(startX, startY, bodyWidth, bodyHeight);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);

        Text label = new Text("3-to-8\nDecoder");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setX(startX + bodyWidth / 2 - label.getLayoutBounds().getWidth() / 2);
        label.setY(startY + bodyHeight / 2);
        circuitPane.getChildren().addAll(body, label);

        // Input Buttons and Wires
        for (int i = 0; i < 3; i++) {
            final int index = i;
            double yPos = startY + 60 + i * 60;

            Button btn = new Button("0");
            btn.setPrefWidth(50);
            btn.setLayoutX(50);
            btn.setLayoutY(yPos - 12);
            btn.setOnAction(e -> {
                inputs[index] = !inputs[index];
                updateVisuals();
            });
            inputButtons[i] = btn;

            Label inputLabel = new Label("A" + (2 - i));
            inputLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
            inputLabel.setTextFill(Color.WHITE);
            inputLabel.setLayoutX(10);
            inputLabel.setLayoutY(yPos - 10);

            Line wire = new Line(100, yPos, startX, yPos);
            wire.setStroke(WIRE_COLOR);
            wire.setStrokeWidth(3);

            circuitPane.getChildren().addAll(inputLabel, btn, wire);
        }

        // Enable Button and Wire
        enableButton = new Button("1");
        enableButton.setPrefWidth(50);
        enableButton.setLayoutX(startX + bodyWidth / 2 - 25);
        enableButton.setLayoutY(10);
        enableButton.setOnAction(e -> {
            enableInput = !enableInput;
            updateVisuals();
        });

        Label enableLabel = new Label("E");
        enableLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        enableLabel.setTextFill(Color.WHITE);
        enableLabel.setLayoutX(enableButton.getLayoutX() + 20);
        enableLabel.setLayoutY(enableButton.getLayoutY() - 25);

        Line enableWire = new Line(enableButton.getLayoutX() + 25, 35, enableButton.getLayoutX() + 25, startY);
        enableWire.setStroke(WIRE_COLOR);
        enableWire.setStrokeWidth(3);

        circuitPane.getChildren().addAll(enableLabel, enableButton, enableWire);

        // Output LEDs and Wires
        for (int i = 0; i < 8; i++) {
            double yPos = startY + 25 + i * 40;

            Circle led = new Circle(15);
            led.setLayoutX(startX + bodyWidth + 50);
            led.setLayoutY(yPos);
            outputLeds[i] = led;

            Label outputLabel = new Label("Y" + i);
            outputLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
            outputLabel.setTextFill(Color.WHITE);
            outputLabel.setLayoutX(led.getLayoutX() + 25);
            outputLabel.setLayoutY(yPos - 12);

            Line wire = new Line(startX + bodyWidth, yPos, led.getLayoutX(), yPos);
            wire.setStroke(WIRE_COLOR);
            wire.setStrokeWidth(3);

            circuitPane.getChildren().addAll(led, outputLabel, wire);
        }
    }

    private void updateVisuals() {
        // Update input buttons
        for (int i = 0; i < 3; i++) {
            inputButtons[i].setText(inputs[i] ? "1" : "0");
            inputButtons[i].setStyle(inputs[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
        }

        // Update enable button
        enableButton.setText(enableInput ? "1" : "0");
        enableButton.setStyle(enableInput ? ACTIVE_STYLE : INACTIVE_STYLE);

        // Get the active output from the logic
        int activeIndex = decoder.getActiveOutput(enableInput, inputs[0], inputs[1], inputs[2]);

        // Update output LEDs
        for (int i = 0; i < 8; i++) {
            outputLeds[i].setFill(i == activeIndex ? LED_ON_COLOR : LED_OFF_COLOR);
        }
    }
}
