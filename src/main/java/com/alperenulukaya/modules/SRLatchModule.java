package com.alperenulukaya.modules;

import com.alperenulukaya.logic.SRLatch;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module to demonstrate the behavior of an SR Latch.
 * Provides interactive buttons for S and R inputs and visual feedback, including a characteristic table.
 */
public class SRLatchModule {

    private final VBox view;
    private final SRLatch latch = new SRLatch();

    // --- UI Elements ---
    private Circle qLed, qNotLed;
    private Button sButton, rButton;
    private GridPane truthTable; // GÜNCELLEME 1
    
    // --- UI Constants ---
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 18px; -fx-text-fill: white;";
    private final String BUTTON_INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 18px; -fx-text-fill: white;";
    private final String HIGHLIGHT_STYLE = "-fx-background-color: #0066CC;";
    private final String INVALID_HIGHLIGHT_STYLE = "-fx-background-color: #A62424;"; // Red for invalid state

    public SRLatchModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        
        Label title = createTitleArea();
        HBox interactionArea = createInteractionArea();

        view.getChildren().addAll(title, interactionArea);
        
        Button resetButton = new Button("Reset Latch");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> {
            latch.reset();
            handleInput(false, false);
        });
        view.getChildren().add(resetButton);

        updateUI(false, false);
    }

    public Node getView() {
        return view;
    }
    
    public void stopTimeline() {
        // No timeline to stop
    }

    private Label createTitleArea() {
        Label title = new Label("SR Latch Simulator (NOR based)");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));
        return title;
    }

    private HBox createInteractionArea() {
        VBox inputsAndCircuit = new VBox(30, createInputControls(), createCircuitDiagram());
        inputsAndCircuit.setAlignment(Pos.CENTER);
        
        // GÜNCELLEME 2: Right panel for outputs and the new table
        VBox rightPanel = new VBox(30, createOutputDisplays(), createTruthTable());
        rightPanel.setAlignment(Pos.CENTER);
        
        HBox mainBox = new HBox(50, inputsAndCircuit, rightPanel);
        mainBox.setAlignment(Pos.CENTER);
        return mainBox;
    }

    private VBox createInputControls() {
        sButton = new Button("S=0");
        sButton.setStyle(BUTTON_INACTIVE_STYLE);
        sButton.setPrefWidth(100);

        rButton = new Button("R=0");
        rButton.setStyle(BUTTON_INACTIVE_STYLE);
        rButton.setPrefWidth(100);

        sButton.setOnMousePressed(e -> handleInput(true, rButton.isArmed()));
        sButton.setOnMouseReleased(e -> handleInput(false, rButton.isArmed()));
        
        rButton.setOnMousePressed(e -> handleInput(sButton.isArmed(), true));
        rButton.setOnMouseReleased(e -> handleInput(sButton.isArmed(), false));
        
        HBox buttonBox = new HBox(20, sButton, rButton);
        buttonBox.setAlignment(Pos.CENTER);
        return new VBox(buttonBox);
    }

    private HBox createOutputDisplays() {
        Label qLabel = new Label("Q");
        qLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        qLabel.setTextFill(Color.WHITE);
        qLed = new Circle(30, LED_OFF_COLOR);
        qLed.setStroke(Color.BLACK);
        
        Label qNotLabel = new Label("Q'");
        qNotLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        qNotLabel.setTextFill(Color.WHITE);
        qNotLed = new Circle(30, LED_OFF_COLOR);
        qNotLed.setStroke(Color.BLACK);

        VBox qBox = new VBox(10, qLabel, qLed);
        qBox.setAlignment(Pos.CENTER);
        VBox qNotBox = new VBox(10, qNotLabel, qNotLed);
        qNotBox.setAlignment(Pos.CENTER);
        
        return new HBox(30, qBox, qNotBox);
    }
    
    private Pane createCircuitDiagram() {
        Pane pane = new Pane();
        pane.setPrefSize(250, 200);
        Rectangle body = new Rectangle(50, 50, 150, 100);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);
        
        Text blockLabel = new Text("SR Latch");
        blockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        blockLabel.relocate(85, 90);
        
        Line sLine = new Line(0, 75, 50, 75);
        Line rLine = new Line(0, 125, 50, 125);
        Line qLine = new Line(200, 75, 250, 75);
        Line qNotLine = new Line(200, 125, 250, 125);
        
        for(Line line : new Line[]{sLine, rLine, qLine, qNotLine}){
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(3);
        }
        
        Text sText = new Text( -20, 80, "S"); sText.setFont(Font.font(20)); sText.setFill(Color.WHITE);
        Text rText = new Text( -20, 130, "R"); rText.setFont(Font.font(20)); rText.setFill(Color.WHITE);
        
        pane.getChildren().addAll(body, blockLabel, sLine, rLine, qLine, qNotLine, sText, rText);
        return pane;
    }
    
    // GÜNCELLEME 3: Add method to create the characteristic table
    private VBox createTruthTable() {
        truthTable = new GridPane();
        truthTable.setAlignment(Pos.CENTER);
        truthTable.setHgap(15);
        truthTable.setVgap(5);
        truthTable.setPadding(new Insets(10));
        truthTable.setStyle("-fx-background-color: #44474A; -fx-background-radius: 10;");

        String[] headers = {"S", "R", "Q(t+1)", "Action"};
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
            {"1", "1", "0", "Invalid"}
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

    private void handleInput(boolean sValue, boolean rValue) {
        sButton.setText("S=" + (sValue ? "1" : "0"));
        sButton.setStyle(sValue ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        rButton.setText("R=" + (rValue ? "1" : "0"));
        rButton.setStyle(rValue ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        latch.update(sValue, rValue);
        updateUI(sValue, rValue);
    }
    
    private void updateUI(boolean s, boolean r) {
        qLed.setFill(latch.getQ() ? LED_ON_COLOR : LED_OFF_COLOR);
        qNotLed.setFill(latch.getQNot() ? LED_ON_COLOR : LED_OFF_COLOR);
        
        // GÜNCELLEME 4: Highlight the table row
        for (Node node : truthTable.getChildren()) {
            if (GridPane.getRowIndex(node) > 0) {
                node.setStyle("-fx-background-color: transparent;");
            }
        }
        
        int rowIndex = (s ? 2 : 0) + (r ? 1 : 0);
        String style = (s && r) ? INVALID_HIGHLIGHT_STYLE : HIGHLIGHT_STYLE;

        for (Node node : truthTable.getChildren()) {
            if (GridPane.getRowIndex(node) == rowIndex + 1) {
                node.setStyle(style);
            }
        }
    }
}