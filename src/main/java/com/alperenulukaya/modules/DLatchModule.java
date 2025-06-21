
package com.alperenulukaya.modules;

import java.util.Map;

import com.alperenulukaya.logic.DLatch;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module to demonstrate the behavior of a Gated D Latch,
 * now including a real-time timing diagram.
 */
public class DLatchModule {

    private VBox view;
    private DLatch latch = new DLatch();

    // --- UI Elements ---
    private Circle qLed;
    private Text stateDescription;
    private Button dButton, eButton;
    private TimingDiagram timingDiagram; 
    
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
        
        VBox leftPanel = createLeftPanel(); // Inputs and circuit
        VBox rightPanel = createRightPanel(); // Outputs and timing diagram
        HBox interactionArea = new HBox(50, leftPanel, rightPanel);
        interactionArea.setAlignment(Pos.CENTER);

        view.getChildren().addAll(title, interactionArea);
        
        Button resetButton = new Button("Reset Latch & Diagram");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> {
            latch.reset();
            dInput = false;
            eInput = false;
            timingDiagram.clear(); // Clear the diagram history
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
        Label title = new Label("Gated D Latch with Timing Diagram");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));
        return title;
    }
    
    // Helper method for the new layout
    private VBox createLeftPanel() {
        VBox inputs = createInputControls();
        Pane circuit = createCircuitDiagram();
        
        VBox leftPanel = new VBox(40, inputs, circuit);
        leftPanel.setAlignment(Pos.CENTER);
        return leftPanel;
    }
    
    // Helper method for the new layout
    private VBox createRightPanel() {
        VBox outputs = createOutputDisplays();
        
        // Initialize the timing diagram
        timingDiagram = new TimingDiagram(500, "E", "D", "Q");
        
        VBox rightPanel = new VBox(40, outputs, timingDiagram.getCanvas());
        rightPanel.setAlignment(Pos.CENTER);
        return rightPanel;
    }

    private VBox createInputControls() {
        dButton = new Button("D=0");
        dButton.setStyle(BUTTON_INACTIVE_STYLE);
        dButton.setPrefWidth(120);

        eButton = new Button("E=0");
        eButton.setStyle(BUTTON_INACTIVE_STYLE);
        eButton.setPrefWidth(120);

        dButton.setOnAction(e -> {
            dInput = !dInput;
            updateInputsAndLogic();
        });
        
        eButton.setOnAction(e -> {
            eInput = !eInput;
            updateInputsAndLogic();
        });
        
        HBox buttonBox = new HBox(20, dButton, eButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        VBox inputBox = new VBox(10, buttonBox);
        inputBox.setAlignment(Pos.CENTER);
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
        
        HBox qBox = new HBox(20, qLabel, qLed);
        qBox.setAlignment(Pos.CENTER);
        
        VBox outputBox = new VBox(20, qBox, stateDescription);
        outputBox.setAlignment(Pos.CENTER);
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
        dButton.setText("D=" + (dInput ? "1" : "0"));
        dButton.setStyle(dInput ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        eButton.setText("E=" + (eInput ? "1" : "0"));
        eButton.setStyle(eInput ? BUTTON_ACTIVE_STYLE : BUTTON_INACTIVE_STYLE);
        
        latch.update(dInput, eInput);
        
        timingDiagram.addState(Map.of(
            "E", eInput,
            "D", dInput,
            "Q", latch.getQ()
        ));
        
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