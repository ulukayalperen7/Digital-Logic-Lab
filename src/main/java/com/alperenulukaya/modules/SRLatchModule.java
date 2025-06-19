
package com.alperenulukaya.modules;

import com.alperenulukaya.logic.SRLatch;

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
 * A UI module to demonstrate the behavior of an SR Latch.
 * Provides interactive buttons for S and R inputs and visual feedback for Q and Q_not outputs.
 */
public class SRLatchModule {

    private VBox view;
    private SRLatch latch = new SRLatch();

    // --- UI Elements ---
    private Circle qLed, qNotLed;
    private Text stateDescription;
    private Button sButton, rButton;

    // --- UI Constants ---
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 18px; -fx-text-fill: white;"; // Green
    private final String BUTTON_INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 18px; -fx-text-fill: white;"; // Dark Gray

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
            handleInput(false, false); // Update buttons and UI based on S=0, R=0
        });
        view.getChildren().add(resetButton);

        updateUI(false, false);
    }

    public Node getView() {
        return view;
    }
    
    public void stopTimeline() {
        // No timeline to stop in this module
    }

    private Label createTitleArea() {
        Label title = new Label("SR Latch Simulator (NOR based)");
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
        Label sLabel = new Label("S (Set)");
        sLabel.setFont(Font.font(20));
        sLabel.setTextFill(Color.WHITE);
        sButton = new Button("S=0");
        sButton.setStyle(BUTTON_INACTIVE_STYLE);
        sButton.setPrefWidth(100);

        Label rLabel = new Label("R (Reset)");
        rLabel.setFont(Font.font(20));
        rLabel.setTextFill(Color.WHITE);
        rButton = new Button("R=0");
        rButton.setStyle(BUTTON_INACTIVE_STYLE);
        rButton.setPrefWidth(100);

        // Using mouse press/release to simulate holding a button down
        sButton.setOnMousePressed(e -> handleInput(true, false));
        sButton.setOnMouseReleased(e -> handleInput(false, false));
        
        rButton.setOnMousePressed(e -> handleInput(false, true));
        rButton.setOnMouseReleased(e -> handleInput(false, false));
        
        VBox inputBox = new VBox(10, sLabel, sButton, new Label(), rLabel, rButton);
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
        
        Label qNotLabel = new Label("Q' (Not Q)");
        qNotLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        qNotLabel.setTextFill(Color.WHITE);
        qNotLed = new Circle(30, LED_OFF_COLOR);
        qNotLed.setStroke(Color.BLACK);
        
        stateDescription = new Text("State: Memory (Hold)");
        stateDescription.setFont(Font.font("Consolas", 18));
        stateDescription.setFill(Color.ORANGE);

        VBox outputBox = new VBox(10, qLabel, qLed, new Label(), qNotLabel, qNotLed, new Label(), stateDescription);
        outputBox.setAlignment(Pos.CENTER);
        outputBox.setPadding(new Insets(0, 20, 0, 0));
        return outputBox;
    }
    
    private Pane createCircuitDiagram() {
        Pane pane = new Pane();
        pane.setPrefSize(250, 250);
        Rectangle body = new Rectangle(50, 50, 150, 150);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);
        
        Text blockLabel = new Text("SR Latch");
        blockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        blockLabel.relocate(85, 115);
        
        Line sLine = new Line(0, 75, 50, 75);
        Line rLine = new Line(0, 175, 50, 175);
        Line qLine = new Line(200, 75, 250, 75);
        Line qNotLine = new Line(200, 175, 250, 175);
        
        for(Line line : new Line[]{sLine, rLine, qLine, qNotLine}){
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(3);
        }
        
        Text sText = new Text( -20, 80, "S"); sText.setFont(Font.font(20)); sText.setFill(Color.WHITE);
        Text rText = new Text( -20, 180, "R"); rText.setFont(Font.font(20)); rText.setFill(Color.WHITE);
        Text qText = new Text( 260, 80, "Q"); qText.setFont(Font.font(20)); qText.setFill(Color.WHITE);
        Text qNotText = new Text( 260, 180, "Q'"); qNotText.setFont(Font.font(20)); qNotText.setFill(Color.WHITE);

        pane.getChildren().addAll(body, blockLabel, sLine, rLine, qLine, qNotLine, sText, rText, qText, qNotText);
        
        return pane;
    }

    private void handleInput(boolean sValue, boolean rValue) {
        // Prevent S and R from being active at the same time from a UI perspective
        boolean s, r;
        if(sValue && rValue) { // Should not happen with current setup but good practice
            s = false; r = false;
        } else {
            s = sValue;
            r = rValue;
        }

        sButton.setStyle(s ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        rButton.setStyle(r ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        sButton.setText("S=" + (s ? "1" : "0"));
        rButton.setText("R=" + (r ? "1" : "0"));
        
        latch.update(s, r);
        updateUI(s, r);
    }
    
    private void updateUI(boolean s, boolean r) {
        // Since the latch logic handles the state, we just read from it.
        boolean qState = latch.getQ();
        boolean qNotState = latch.getQNot();
        
        qLed.setFill(qState ? LED_ON_COLOR : LED_OFF_COLOR);
        qNotLed.setFill(qNotState ? LED_ON_COLOR : LED_OFF_COLOR);
        
        if (s && !r) {
            stateDescription.setText("State: SET");
            stateDescription.setFill(Color.LIGHTGREEN);
        } else if (!s && r) {
            stateDescription.setText("State: RESET");
            stateDescription.setFill(Color.TOMATO);
        } else if (s && r) {
            // This case is handled by our latch logic, but we describe it.
            // In a real NOR latch, both would go low.
            stateDescription.setText("State: INVALID (Forbidden)");
            stateDescription.setFill(Color.RED);
            qLed.setFill(LED_OFF_COLOR);
            qNotLed.setFill(LED_OFF_COLOR);
        } else { // !s && !r
            stateDescription.setText("State: MEMORY (Hold)");
            stateDescription.setFill(Color.ORANGE);
        }
    }
}