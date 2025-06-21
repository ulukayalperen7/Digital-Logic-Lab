package com.alperenulukaya.modules;

import com.alperenulukaya.logic.FullAdder;

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
 * A UI module to demonstrate a 1-bit Full Adder.
 * This version contains fixes for wire positioning and alignment.
 */
public class FullAdderModule {

    private final VBox view;
    private final FullAdder fullAdder = new FullAdder();

    private final Button[] inputButtons = new Button[3];
    private final Circle[] outputLeds = new Circle[2];
    private Pane circuitPane;

    private final boolean[] inputs = new boolean[3]; // 0: A, 1: B, 2: C-in

    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 16px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 16px; -fx-text-fill: white;";
    private final Color LED_ON_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color WIRE_ACTIVE_COLOR = Color.LIMEGREEN;
    private final Color WIRE_INACTIVE_COLOR = Color.GRAY;

    public FullAdderModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("1-Bit Full Adder");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        circuitPane = createCircuitPane();
        
        view.getChildren().addAll(title, circuitPane);
        updateVisuals();
    }

    public Node getView() { return view; }
    public void stopTimeline() { /* No timeline in this module */ }

    private Pane createCircuitPane() {
        Pane pane = new Pane();
        pane.setPrefSize(600, 300);

        double startX = 200;
        double startY = 50;
        double bodyWidth = 150;
        double bodyHeight = 200;

        VBox bodyBox = new VBox();
        bodyBox.setAlignment(Pos.CENTER);
        bodyBox.setPrefSize(bodyWidth, bodyHeight);
        bodyBox.setStyle("-fx-background-color: #6495ED; -fx-border-color: black;");
        bodyBox.setLayoutX(startX);
        bodyBox.setLayoutY(startY);
        Text label = new Text("Full\nAdder");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        bodyBox.getChildren().add(label);
        pane.getChildren().add(bodyBox);

        String[] inputLabels = {"A", "B", "C-in"};
        for (int i = 0; i < 3; i++) {
            final int index = i;
            double yPos = startY + 40 + i * 60;
            
            Button btn = createToggleButton("0", 50, e -> {
                inputs[index] = !inputs[index];
                updateVisuals();
            });
            inputButtons[i] = btn;
            
            HBox inputBox = createLabeledComponent(new Label(inputLabels[i]), btn);
            inputBox.setLayoutX(50);
            inputBox.setLayoutY(yPos - 12);
            pane.getChildren().add(inputBox);
        }

        String[] outputLabels = {"Sum (S)", "Carry-out (Co)"};
        for (int i = 0; i < 2; i++) {
            double yPos = startY + 60 + i * 80;
            
            outputLeds[i] = new Circle(20, LED_OFF_COLOR);
            outputLeds[i].setStroke(Color.BLACK);
            
            HBox outputBox = createLabeledComponent(outputLeds[i], new Label(outputLabels[i]));
            outputBox.setLayoutX(startX + bodyWidth + 20);
            outputBox.setLayoutY(yPos - 15);
            pane.getChildren().add(outputBox);
        }

        return pane;
    }

    private void drawWires() {
        circuitPane.getChildren().removeIf(node -> node instanceof Line);

        double startX = 200;
        double startY = 50;
        double bodyWidth = 150;

        // Input Wires
        for (int i = 0; i < 3; i++) {
            double yPos = startY + 40 + i * 60;
            double wireStartX = 165; 
            Line wire = new Line(wireStartX, yPos, startX, yPos);
            wire.setStrokeWidth(3);
            wire.setStroke(inputs[i] ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
            circuitPane.getChildren().add(wire);
        }

        // Output Wires
        HBox sumBox = (HBox) outputLeds[0].getParent();
        HBox carryBox = (HBox) outputLeds[1].getParent();

        double sumWireY = sumBox.getLayoutY() + outputLeds[0].getRadius();
        Line sumWire = new Line(startX + bodyWidth, sumWireY, sumBox.getLayoutX(), sumWireY);
        sumWire.setStrokeWidth(3);
        sumWire.setStroke(fullAdder.getSum() ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        
        double carryWireY = carryBox.getLayoutY() + outputLeds[1].getRadius();
        Line carryWire = new Line(startX + bodyWidth, carryWireY, carryBox.getLayoutX(), carryWireY);
        carryWire.setStrokeWidth(3);
        carryWire.setStroke(fullAdder.getCarryOut() ? WIRE_ACTIVE_COLOR : WIRE_INACTIVE_COLOR);
        
        circuitPane.getChildren().addAll(sumWire, carryWire);
    }
    
    private void updateVisuals() {
        for (int i = 0; i < 3; i++) {
            inputButtons[i].setText(inputs[i] ? "1" : "0");
            inputButtons[i].setStyle(inputs[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
        }

        fullAdder.update(inputs[0], inputs[1], inputs[2]);
        
        outputLeds[0].setFill(fullAdder.getSum() ? LED_ON_COLOR : LED_OFF_COLOR);
        outputLeds[1].setFill(fullAdder.getCarryOut() ? LED_ON_COLOR : LED_OFF_COLOR);
        
        drawWires();
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

    private HBox createLabeledComponent(Label label, Node control) {
        label.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        label.setTextFill(Color.WHITE);
        HBox box = new HBox(10, label, control);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }
}