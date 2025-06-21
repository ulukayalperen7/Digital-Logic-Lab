package com.alperenulukaya.modules;

import java.util.Map;

import com.alperenulukaya.logic.T_FlipFlop;
import com.alperenulukaya.util.TimingDiagram;

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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module to demonstrate the behavior of a T (Toggle) Flip-Flop, including
 * a real-time timing diagram.
 */
public class TFlipFlopModule {

    private final VBox view;
    private final T_FlipFlop flipFlop = new T_FlipFlop();

    // UI Elements
    private Circle qLed, qNotLed;
    private Button tButton, clockButton;
    private Text stateDescription;
    private TimingDiagram timingDiagram;

    private boolean tInput = false;

    // UI Constants
    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;

    public TFlipFlopModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = createTitleArea();

        // --- Redesigned Layout ---
        HBox mainInteractionArea = new HBox(50);
        mainInteractionArea.setAlignment(Pos.CENTER);

        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        mainInteractionArea.getChildren().addAll(leftPanel, rightPanel);

        view.getChildren().addAll(title, mainInteractionArea);

        Button resetButton = new Button("Reset Flip-Flop & Diagram");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> {
            flipFlop.reset();
            tInput = false;
            timingDiagram.clear();
            updateInputsAndLogic(false);
        });
        view.getChildren().add(resetButton);

        updateInputsAndLogic(false); // Initial state
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        /* No timeline */ }

    private Label createTitleArea() {
        Label title = new Label("T (Toggle) Flip-Flop Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));
        return title;
    }

    private VBox createLeftPanel() {
        Pane circuit = createCircuitDiagram();
        clockButton = new Button("Clock Pulse");
        clockButton.setFont(Font.font(18));
        clockButton.setPrefWidth(250);
        clockButton.setOnAction(e -> handleClockPulse());

        VBox leftPanel = new VBox(20, circuit, clockButton);
        leftPanel.setAlignment(Pos.CENTER);
        return leftPanel;
    }

    private VBox createRightPanel() {
        VBox outputs = createOutputDisplays();
        timingDiagram = new TimingDiagram(600, "CLK", "T", "Q");

        VBox rightPanel = new VBox(30, outputs, timingDiagram.getCanvas());
        rightPanel.setAlignment(Pos.CENTER);
        return rightPanel;
    }

    private VBox createOutputDisplays() {
        VBox qBox = new VBox(10, new Label("Q"), qLed = new Circle(30, LED_OFF_COLOR));
        qBox.setAlignment(Pos.CENTER);

        VBox qNotBox = new VBox(10, new Label("Q'"), qNotLed = new Circle(30, LED_OFF_COLOR));
        qNotBox.setAlignment(Pos.CENTER);

        qLed.setStroke(Color.BLACK);
        qNotLed.setStroke(Color.BLACK);

        qBox.getChildren().get(0).setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        qNotBox.getChildren().get(0).setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        stateDescription = new Text("Action: Hold");
        stateDescription.setFont(Font.font("Consolas", 18));

        HBox outputHBox = new HBox(40, qBox, qNotBox);
        outputHBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(20, outputHBox, stateDescription);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private Pane createCircuitDiagram() {
        Pane pane = new Pane();
        pane.setPrefSize(250, 150);
        Rectangle body = new Rectangle(50, 25, 150, 100);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);

        Polygon clockTriangle = new Polygon(35, 70, 50, 80, 35, 90);
        clockTriangle.setFill(Color.BLACK);

        Line tLine = new Line(0, 50, 50, 50);
        Line clkLine = new Line(35, 80, 0, 80);
        Line qLine = new Line(200, 50, 250, 50);
        Line qNotLine = new Line(200, 100, 250, 100);

        for (Line line : new Line[]{tLine, clkLine, qLine, qNotLine}) {
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(3);
        }

        tButton = new Button("T=0");
        tButton.setStyle(INACTIVE_STYLE);
        tButton.setPrefWidth(120);
        tButton.setLayoutX(-70);
        tButton.setLayoutY(35);
        tButton.setOnAction(e -> {
            tInput = !tInput;
            updateInputsAndLogic(true);
        });

        Text qText = new Text(260, 55, "Q");
        qText.setFont(Font.font(20));
        qText.setFill(Color.WHITE);
        Text qNotText = new Text(260, 105, "Q'");
        qNotText.setFont(Font.font(20));
        qNotText.setFill(Color.WHITE);

        pane.getChildren().addAll(body, clockTriangle, tLine, clkLine, qLine, qNotLine, tButton, qText, qNotText);
        return pane;
    }

    private void handleClockPulse() {
        // Add the state right before the clock pulse (CLK is low)
        timingDiagram.addState(Map.of("CLK", false, "T", tInput, "Q", flipFlop.getQ()));

        // Apply the clock pulse to the logic
        flipFlop.clock();

        // Add the state right after the clock pulse (CLK is high, Q may have changed)
        timingDiagram.addState(Map.of("CLK", true, "T", tInput, "Q", flipFlop.getQ()));

        updateUI();
    }

    private void updateInputsAndLogic(boolean addStateToDiagram) {
        tButton.setText("T=" + (tInput ? "1" : "0"));
        tButton.setStyle(tInput ? ACTIVE_STYLE : INACTIVE_STYLE);
        flipFlop.setInput(tInput);

        if (addStateToDiagram) {
            // Show the change in T input on the diagram before the next clock
            timingDiagram.addState(Map.of("CLK", false, "T", tInput, "Q", flipFlop.getQ()));
        }

        updateUI();
    }

    private void updateUI() {
        qLed.setFill(flipFlop.getQ() ? LED_ON_COLOR : LED_OFF_COLOR);
        qNotLed.setFill(!flipFlop.getQ() ? LED_ON_COLOR : LED_OFF_COLOR);

        if (tInput) {
            stateDescription.setText("Action: Toggle");
            stateDescription.setFill(Color.LIMEGREEN);
        } else {
            stateDescription.setText("Action: Hold");
            stateDescription.setFill(Color.ORANGE);
        }
    }
}
