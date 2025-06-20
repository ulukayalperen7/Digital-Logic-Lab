
package com.alperenulukaya.modules;

import java.util.Map;

import com.alperenulukaya.logic.MasterSlaveDFlipFlop;
import com.alperenulukaya.util.TimingDiagram;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A UI module to demonstrate a Master-Slave D Flip-Flop.
 * Visualizes the internal master and final slave outputs.
 */
public class MasterSlaveModule {

    private final VBox view;
    private final MasterSlaveDFlipFlop ff = new MasterSlaveDFlipFlop();

    private Button dButton, clkButton;
    private Circle masterLed, slaveLed;
    private TimingDiagram timingDiagram;
    private Text stateDescription;

    private boolean dInput = false;
    private boolean clkInput = false;

    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color MASTER_LED_ON_COLOR = Color.ORANGE;

    public MasterSlaveModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = createTitleArea();
        HBox mainArea = createMainArea();
        timingDiagram = new TimingDiagram(800, "CLK", "D", "Qm", "Q");
        
        Button resetButton = new Button("Reset Flip-Flop & Diagram");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> {
            ff.reset();
            dInput = false;
            clkInput = false;
            timingDiagram.clear();
            updateInputsAndLogic();
        });

        view.getChildren().addAll(title, mainArea, timingDiagram.getCanvas(), resetButton);
        updateInputsAndLogic();
    }

    public Node getView() { return view; }
    public void stopTimeline() { /* No timeline */ }

    private Label createTitleArea() {
        Label title = new Label("Master-Slave D Flip-Flop");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        return title;
    }

    private HBox createMainArea() {
        VBox inputs = createInputControls();
        VBox circuit = createCircuitDiagram();
        
        HBox mainArea = new HBox(50, inputs, circuit);
        mainArea.setAlignment(Pos.CENTER);
        mainArea.setPadding(new Insets(20));
        return mainArea;
    }

    private VBox createInputControls() {
        dButton = new Button("D = 0");
        dButton.setPrefWidth(120);
        dButton.setOnAction(e -> {
            dInput = !dInput;
            updateInputsAndLogic();
        });

        clkButton = new Button("CLK = 0");
        clkButton.setPrefWidth(120);
        clkButton.setOnAction(e -> {
            clkInput = !clkInput;
            updateInputsAndLogic();
        });

        stateDescription = new Text();
        stateDescription.setFont(Font.font("Consolas", 16));
        
        VBox inputs = new VBox(20, dButton, clkButton, stateDescription);
        inputs.setAlignment(Pos.CENTER_LEFT);
        return inputs;
    }

    private VBox createCircuitDiagram() {
        HBox circuit = new HBox(5);
        circuit.setAlignment(Pos.CENTER);
        
        VBox masterBox = createLatchBox("Master", "Qm");
        masterLed = (Circle) masterBox.lookup("#led");

        VBox slaveBox = createLatchBox("Slave", "Q");
        slaveLed = (Circle) slaveBox.lookup("#led");

        Line connector = new Line(0, 0, 20, 0);
        connector.setStroke(Color.GRAY);
        connector.setStrokeWidth(3);

        circuit.getChildren().addAll(masterBox, connector, slaveBox);
        return new VBox(circuit);
    }
    
    private VBox createLatchBox(String name, String outputLabel) {
        Rectangle body = new Rectangle(120, 150, Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.WHITE); // Make the label text white and prominent
        
        Circle led = new Circle(20, LED_OFF_COLOR);
        led.setId("led"); // Assign an ID to find it later
        led.setStroke(Color.BLACK);
        
        Label outLabel = new Label(outputLabel);
        outLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        outLabel.setTextFill(Color.WHITE);
        
        HBox outputBox = new HBox(10, led, outLabel);
        outputBox.setAlignment(Pos.CENTER);
        
        VBox latchBox = new VBox(10, nameLabel, body, outputBox);
        latchBox.setAlignment(Pos.CENTER);
        return latchBox;
    }
    
    private void updateInputsAndLogic() {
        ff.update(dInput, clkInput);
        
        timingDiagram.addState(Map.of(
            "CLK", clkInput,
            "D", dInput,
            "Qm", ff.getMasterQ(),
            "Q", ff.getQ()
        ));
        
        updateVisuals();
    }
    
    private void updateVisuals() {
        dButton.setText("D = " + (dInput ? "1" : "0"));
        dButton.setStyle(dInput ? ACTIVE_STYLE : INACTIVE_STYLE);
        
        clkButton.setText("CLK = " + (clkInput ? "1" : "0"));
        clkButton.setStyle(clkInput ? ACTIVE_STYLE : INACTIVE_STYLE);
        
        masterLed.setFill(ff.getMasterQ() ? MASTER_LED_ON_COLOR : LED_OFF_COLOR);
        slaveLed.setFill(ff.getQ() ? LED_ON_COLOR : LED_OFF_COLOR);
        
        if (clkInput) {
            stateDescription.setText("Clock is HIGH.\nMaster is transparent.\nSlave is holding.");
            stateDescription.setFill(Color.ORANGE);
        } else {
            stateDescription.setText("Clock is LOW.\nMaster is holding.\nSlave is transparent.");
            stateDescription.setFill(Color.CYAN);
        }
    }
}