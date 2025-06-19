
package com.alperenulukaya.modules;

import com.alperenulukaya.logic.JKFlipFlop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
 * A UI module to demonstrate the behavior of a JK Flip-Flop.
 * Provides interactive controls for J, K, and Clock inputs.
 */
public class JKFlipFlopModule {

    private VBox view;
    private JKFlipFlop flipFlop = new JKFlipFlop();

    // --- UI Elements ---
    private Circle qLed, qNotLed;
    private Button jButton, kButton, clockButton;
    private GridPane truthTable;
    
    // --- State Variables ---
    private boolean jInput = false;
    private boolean kInput = false;

    // --- UI Constants ---
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String BUTTON_INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String HIGHLIGHT_STYLE = "-fx-background-color: #0066CC;";

    public JKFlipFlopModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        
        Label title = createTitleArea();
        HBox interactionArea = createInteractionArea();

        view.getChildren().addAll(title, interactionArea);
        
        Button resetButton = new Button("Reset Flip-Flop");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> {
            flipFlop.reset();
            jInput = false;
            kInput = false;
            updateInputsAndLogic();
        });
        view.getChildren().add(resetButton);

        updateInputsAndLogic();
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        // No timeline to stop
    }

    private Label createTitleArea() {
        Label title = new Label("JK Flip-Flop Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));
        return title;
    }

    private HBox createInteractionArea() {
        VBox inputs = createInputControls();
        VBox outputsAndTable = new VBox(30, createOutputDisplays(), createTruthTable());
        outputsAndTable.setAlignment(Pos.CENTER);
        
        HBox mainBox = new HBox(50, inputs, outputsAndTable);
        mainBox.setAlignment(Pos.CENTER);
        return mainBox;
    }

    private VBox createInputControls() {
        Pane circuit = createCircuitDiagram(); // This pane will contain the buttons

        clockButton = new Button("Clock Pulse");
        clockButton.setFont(Font.font(18));
        clockButton.setPrefWidth(250);
        
        clockButton.setOnAction(e -> {
            flipFlop.clock();
            updateUI();
        });
        
        VBox inputBox = new VBox(20, circuit, clockButton);
        inputBox.setAlignment(Pos.CENTER);
        return inputBox;
    }

    private HBox createOutputDisplays() {
        VBox qBox = new VBox(10, new Label("Q"), qLed = new Circle(30, LED_OFF_COLOR));
        qBox.setAlignment(Pos.CENTER);
        
        VBox qNotBox = new VBox(10, new Label("Q'"), qNotLed = new Circle(30, LED_OFF_COLOR));
        qNotBox.setAlignment(Pos.CENTER);
        
        qLed.setStroke(Color.BLACK);
        qNotLed.setStroke(Color.BLACK);
        
        qBox.getChildren().get(0).setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        qNotBox.getChildren().get(0).setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        HBox outputHBox = new HBox(40, qBox, qNotBox);
        outputHBox.setAlignment(Pos.CENTER);
        return outputHBox;
    }
    
    private Pane createCircuitDiagram() {
        Pane pane = new Pane();
        pane.setPrefSize(250, 200);
        Rectangle body = new Rectangle(50, 25, 150, 150);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);
        
        Polygon clockTriangle = new Polygon(35, 95, 50, 105, 35, 115);
        clockTriangle.setFill(Color.BLACK);
        
        Line jLine = new Line(0, 50, 50, 50);
        Line kLine = new Line(0, 150, 50, 150);
        Line clkLine = new Line(35, 105, 0, 105);
        Line qLine = new Line(200, 50, 250, 50);
        Line qNotLine = new Line(200, 150, 250, 150);
        
        for(Line line : new Line[]{jLine, kLine, clkLine, qLine, qNotLine}){
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(3);
        }

        jButton = new Button("J=0");
        jButton.setStyle(BUTTON_INACTIVE_STYLE);
        jButton.setPrefWidth(120);
        jButton.setLayoutX(-70);
        jButton.setLayoutY(35);

        kButton = new Button("K=0");
        kButton.setStyle(BUTTON_INACTIVE_STYLE);
        kButton.setPrefWidth(120);
        kButton.setLayoutX(-70);
        kButton.setLayoutY(135);

        jButton.setOnAction(e -> {
            jInput = !jInput;
            updateInputsAndLogic();
        });
        
        kButton.setOnAction(e -> {
            kInput = !kInput;
            updateInputsAndLogic();
        });

        Text qText = new Text(260, 55, "Q"); qText.setFont(Font.font(20)); qText.setFill(Color.WHITE);
        Text qNotText = new Text(260, 155, "Q'"); qNotText.setFont(Font.font(20)); qNotText.setFill(Color.WHITE);

        pane.getChildren().addAll(body, clockTriangle, jLine, kLine, clkLine, qLine, qNotLine, jButton, kButton, qText, qNotText);
        return pane;
    }
    
    private VBox createTruthTable() {
        truthTable = new GridPane();
        truthTable.setAlignment(Pos.CENTER);
        truthTable.setHgap(15);
        truthTable.setVgap(5);
        truthTable.setPadding(new Insets(10));
        truthTable.setStyle("-fx-background-color: #44474A; -fx-background-radius: 10;");

        String[] headers = {"J", "K", "Q(t+1)", "Action"};
        for (int i = 0; i < headers.length; i++) {
            Label headerLabel = new Label(headers[i]);
            headerLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
            headerLabel.setTextFill(Color.WHITE);
            truthTable.add(headerLabel, i, 0);
        }

        String[][] data = {
            {"0", "0", "Q(t)", "Hold"},
            {"0", "1", "0", "Reset"},
            {"1", "0", "1", "Set"},
            {"1", "1", "Q(t)'", "Toggle"}
        };

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                Label cell = new Label(data[i][j]);
                cell.setFont(Font.font("Consolas", 14));
                cell.setTextFill(Color.WHITE);
                truthTable.add(cell, j, i + 1);
            }
        }
        
        Label tableTitle = new Label("Characteristic Table");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableTitle.setTextFill(Color.WHITE);

        return new VBox(10, tableTitle, truthTable);
    }
    
    private void updateInputsAndLogic() {
        jButton.setText("J=" + (jInput ? "1" : "0"));
        jButton.setStyle(jInput ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        kButton.setText("K=" + (kInput ? "1" : "0"));
        kButton.setStyle(kInput ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        flipFlop.setInputs(jInput, kInput);
        updateUI();
    }
    
    private void updateUI() {
        qLed.setFill(flipFlop.getQ() ? LED_ON_COLOR : LED_OFF_COLOR);
        qNotLed.setFill(flipFlop.getQNot() ? LED_ON_COLOR : LED_OFF_COLOR);
        
        // This is a cleaner way to highlight rows in a GridPane.
        // First, clear all previous highlights.
        for (Node node : truthTable.getChildren()) {
            if (GridPane.getRowIndex(node) > 0) { // Don't style the header row
                node.setStyle("-fx-background-color: transparent;");
            }
        }
        
        // Then, highlight the current row.
        int rowIndex = (jInput ? 2 : 0) + (kInput ? 1 : 0);
        for (Node node : truthTable.getChildren()) {
            if (GridPane.getRowIndex(node) == rowIndex + 1) {
                node.setStyle(HIGHLIGHT_STYLE);
            }
        }
    }
}