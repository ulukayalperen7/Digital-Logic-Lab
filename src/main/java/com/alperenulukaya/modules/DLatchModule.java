package com.alperenulukaya.modules;


import com.alperenulukaya.logic.DLatch;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module to demonstrate the behavior of a Gated D Latch.
 * Allows interaction with D (Data) and E (Enable) inputs.
 */
public class DLatchModule {

    private VBox view;
    private DLatch latch = new DLatch();

    // --- UI Elements ---
    private Circle qLed;
    private Text stateDescription;
    private Button dButton, eButton;
    
    // --- State Variables ---
    private boolean dInput = false;
    private boolean eInput = false;

    // --- UI Constants ---
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 18px; -fx-text-fill: white;";
    private final String BUTTON_INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 18px; -fx-text-fill: white;";

    public DLatchModule() {
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
            dInput = false;
            eInput = false;
            updateInputsAndLogic();
        });
        view.getChildren().add(resetButton);

        updateInputsAndLogic(); // Initialize UI state
    }

    public Node getView() {
        return view;
    }
    
    public void stopTimeline() {
        // No timeline to stop
    }

    private Label createTitleArea() {
        Label title = new Label("Gated D Latch Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));
        return title;
    }

    private HBox createInteractionArea() {
        VBox inputs = createInputControls();
        Pane circuit = createCircuitDiagram();
        VBox outputs = createOutputDisplays();
        
        HBox mainBox = new HBox(50, inputs, circuit, outputs);
        mainBox.setAlignment(Pos.CENTER);
        return mainBox;
    }

    private VBox createInputControls() {
        Label dLabel = new Label("D (Data)");
        dLabel.setFont(Font.font(20));
        dLabel.setTextFill(Color.WHITE);
        dButton = new Button("D=0");
        dButton.setStyle(BUTTON_INACTIVE_STYLE);
        dButton.setPrefWidth(120);

        Label eLabel = new Label("E (Enable)");
        eLabel.setFont(Font.font(20));
        eLabel.setTextFill(Color.WHITE);
        eButton = new Button("E=0");
        eButton.setStyle(BUTTON_INACTIVE_STYLE);
        eButton.setPrefWidth(120);

        // Toggle buttons
        dButton.setOnAction(e -> {
            dInput = !dInput;
            updateInputsAndLogic();
        });
        
        eButton.setOnAction(e -> {
            eInput = !eInput;
            updateInputsAndLogic();
        });
        
        VBox inputBox = new VBox(10, dLabel, dButton, new Label(), eLabel, eButton);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.setPadding(new Insets(0, 0, 0, 20));
        return inputBox;
    }

    private VBox createOutputDisplays() {
        Label qLabel = new Label("Q");
        qLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        qLabel.setTextFill(Color.WHITE);
        qLed = new Circle(30, LED_OFF_COLOR);
        qLed.setStroke(Color.BLACK);
        
        stateDescription = new Text("State: Opaque (Hold)");
        stateDescription.setFont(Font.font("Consolas", 18));
        stateDescription.setFill(Color.ORANGE);

        VBox outputBox = new VBox(20, qLabel, qLed, new Label(), stateDescription);
        outputBox.setAlignment(Pos.CENTER);
        outputBox.setPadding(new Insets(0, 20, 0, 0));
        return outputBox;
    }
    
    private Pane createCircuitDiagram() {
        Pane pane = new Pane();
        pane.setPrefSize(250, 200);
        Rectangle body = new Rectangle(50, 50, 150, 100);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);
        
        Text blockLabel = new Text("D Latch");
        blockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        blockLabel.relocate(85, 90);
        
        Line dLine = new Line(0, 75, 50, 75);
        Line eLine = new Line(125, 150, 125, 200);
        Line qLine = new Line(200, 75, 250, 75);
        
        for(Line line : new Line[]{dLine, eLine, qLine}){
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(3);
        }
        
        Text dText = new Text( -20, 80, "D"); dText.setFont(Font.font(20)); dText.setFill(Color.WHITE);
        Text eText = new Text( 118, 220, "E"); eText.setFont(Font.font(20)); eText.setFill(Color.WHITE);
        Text qText = new Text( 260, 80, "Q"); qText.setFont(Font.font(20)); qText.setFill(Color.WHITE);

        pane.getChildren().addAll(body, blockLabel, dLine, eLine, qLine, dText, eText, qText);
        
        return pane;
    }

    private void updateInputsAndLogic() {
        // Update button appearance
        dButton.setText("D=" + (dInput ? "1" : "0"));
        dButton.setStyle(dInput ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        eButton.setText("E=" + (eInput ? "1" : "0"));
        eButton.setStyle(eInput ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        // Update the core logic
        latch.update(dInput, eInput);
        
        // Update the entire UI based on the new state
        updateUI();
    }
    
    private void updateUI() {
        qLed.setFill(latch.getQ() ? LED_ON_COLOR : LED_OFF_COLOR);
        
        if (eInput) {
            stateDescription.setText("State: Transparent (Q follows D)");
            stateDescription.setFill(Color.LIGHTGREEN);
        } else {
            stateDescription.setText("State: Opaque (Hold)");
            stateDescription.setFill(Color.ORANGE);
        }
    }
}
