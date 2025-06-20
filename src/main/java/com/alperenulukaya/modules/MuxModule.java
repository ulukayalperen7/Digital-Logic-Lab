// FILE: src/main/java/com/alperenulukaya/modules/MuxModule.java
package com.alperenulukaya.modules;

import java.util.ArrayList;
import java.util.List;

import com.alperenulukaya.logic.Mux4to1;
import com.alperenulukaya.logic.Mux8to1;

import javafx.animation.FillTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
import javafx.util.Duration;

/**
 * The final, polished version of the MUX UI module.
 * Features a larger, clearer layout, correct wiring, and enhanced visual feedback for the active path.
 */
public class MuxModule {

    private enum MuxType { MUX_4_TO_1, MUX_8_TO_1 }
    private MuxType currentMuxType = MuxType.MUX_4_TO_1;

    private final VBox view;
    private final Mux4to1 mux4to1 = new Mux4to1();
    private final Mux8to1 mux8to1 = new Mux8to1();

    private Pane circuitPane;
    private List<Button> dataButtons = new ArrayList<>();
    private List<Button> selectButtons = new ArrayList<>();
    private Circle outputLed;
    private Label selectedIndexLabel;

    private boolean[] dataInputs = new boolean[8];
    private boolean[] selectInputs = new boolean[3];

    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 14px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 14px; -fx-text-fill: white;";
    private final Color PATH_INACTIVE_COLOR = Color.GRAY;
    private final Color PATH_ACTIVE_0_COLOR = Color.CYAN;
    private final Color PATH_ACTIVE_1_COLOR = Color.LIMEGREEN;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;

    public MuxModule() {
        view = new VBox(10);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);
        
        Label title = createTitleArea();
        HBox modeSelector = createModeSelector();
        
        circuitPane = new Pane();
        circuitPane.setPrefSize(800, 600); 

        view.getChildren().addAll(title, modeSelector, circuitPane);
        
        redrawUI();
    }

    public Node getView() { return view; }
    public void stopTimeline() { /* No timeline */ }

    private void setMuxType(MuxType type) {
        if (this.currentMuxType == type) return;
        this.currentMuxType = type;
        for (int i = 0; i < dataInputs.length; i++) dataInputs[i] = false;
        for (int i = 0; i < selectInputs.length; i++) selectInputs[i] = false;
        redrawUI();
    }

    private void redrawUI() {
        circuitPane.getChildren().clear();
        dataButtons.clear();
        selectButtons.clear();

        int numDataInputs = (currentMuxType == MuxType.MUX_4_TO_1) ? 4 : 8;
        int numSelectInputs = (currentMuxType == MuxType.MUX_4_TO_1) ? 2 : 3;

        double muxWidth = 180;
        double muxHeight = 50 + numDataInputs * 50;
        double startX = 300;
        double startY = 50;

        Rectangle body = new Rectangle(startX, startY, muxWidth, muxHeight);
        body.setFill(Color.CORNFLOWERBLUE);
        body.setStroke(Color.BLACK);
        
        String label = (numDataInputs == 4) ? "4-to-1\nMUX" : "8-to-1\nMUX";
        Text muxLabel = new Text(startX + muxWidth / 2 - 45, startY + muxHeight / 2 - 25, label);
        muxLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        circuitPane.getChildren().addAll(body, muxLabel);
        
        for (int i = 0; i < numDataInputs; i++) {
            final int index = i;
            double yPos = startY + 30 + i * 50;
            
            Label inputLabel = new Label("I" + i);
            inputLabel.setFont(Font.font("Consolas", 18));
            inputLabel.setTextFill(Color.WHITE);
            inputLabel.setLayoutX(startX - 130);
            inputLabel.setLayoutY(yPos - 5);

            Button btn = new Button("0");
            btn.setStyle(INACTIVE_STYLE);
            btn.setPrefWidth(50);
            btn.setLayoutX(startX - 80);
            btn.setLayoutY(yPos - 10);
            btn.setOnAction(e -> {
                dataInputs[index] = !dataInputs[index];
                updateVisuals();
            });
            dataButtons.add(btn);
            circuitPane.getChildren().addAll(inputLabel, btn);
        }
        
        double buttonWidth = 50;
        double spacing = 80;
        double totalGroupWidth = (numSelectInputs * buttonWidth) + ((numSelectInputs - 1) * (spacing - buttonWidth));
        double selectStartX = (startX + muxWidth / 2) - (totalGroupWidth / 2);
        
        for (int i = 0; i < numSelectInputs; i++) {
            final int index = i;
            Button btn = new Button("0");
            btn.setStyle(INACTIVE_STYLE);
            btn.setPrefWidth(buttonWidth);
            btn.setLayoutX(selectStartX + i * spacing);
            btn.setLayoutY(startY + muxHeight + 25);
            btn.setOnAction(e -> {
                selectInputs[index] = !selectInputs[index];
                updateVisuals();
            });
            
            Label selectLabel = new Label("S" + (numSelectInputs - 1 - i));
            selectLabel.setFont(Font.font("Consolas", 18));
            selectLabel.setTextFill(Color.WHITE);
            selectLabel.setLayoutX(btn.getLayoutX() + 15);
            selectLabel.setLayoutY(btn.getLayoutY() + 35);
            
            selectButtons.add(btn);
            circuitPane.getChildren().addAll(selectLabel, btn);
        }
        
        double outputY = startY + muxHeight / 2;
        outputLed = new Circle(30, LED_OFF_COLOR);
        outputLed.setStroke(Color.BLACK);
        outputLed.setLayoutX(startX + muxWidth + 120);
        outputLed.setLayoutY(outputY);

        Label yLabel = new Label("Y");
        yLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        yLabel.setTextFill(Color.WHITE);
        yLabel.setLayoutX(outputLed.getLayoutX() + 45);
        yLabel.setLayoutY(outputY - 20);

        selectedIndexLabel = new Label();
        selectedIndexLabel.setFont(Font.font("Consolas", 16));
        selectedIndexLabel.setTextFill(Color.ORANGE);
        selectedIndexLabel.setLayoutX(outputLed.getLayoutX() - 40);
        selectedIndexLabel.setLayoutY(outputY + 40);
        
        circuitPane.getChildren().addAll(outputLed, yLabel, selectedIndexLabel);

        updateVisuals();
    }

    private void updateVisuals() {
        int numDataInputs = (currentMuxType == MuxType.MUX_4_TO_1) ? 4 : 8;
        int numSelectInputs = (currentMuxType == MuxType.MUX_4_TO_1) ? 2 : 3;

        for (int i = 0; i < numDataInputs; i++) {
            dataButtons.get(i).setText(dataInputs[i] ? "1" : "0");
            dataButtons.get(i).setStyle(dataInputs[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
        }
        for (int i = 0; i < numSelectInputs; i++) {
            selectButtons.get(i).setText(selectInputs[i] ? "1" : "0");
            selectButtons.get(i).setStyle(selectInputs[i] ? ACTIVE_STYLE : INACTIVE_STYLE);
        }
        
        boolean output;
        int activeIndex;
        if (currentMuxType == MuxType.MUX_4_TO_1) {
            boolean[] s = {selectInputs[0], selectInputs[1]};
            boolean[] d = {dataInputs[0], dataInputs[1], dataInputs[2], dataInputs[3]};
            output = mux4to1.getOutput(d, s);
            activeIndex = (s[0] ? 2 : 0) + (s[1] ? 1 : 0);
        } else {
            boolean[] s = {selectInputs[0], selectInputs[1], selectInputs[2]};
            output = mux8to1.getOutput(dataInputs, s[0], s[1], s[2]);
            activeIndex = (s[0] ? 4 : 0) + (s[1] ? 2 : 0) + (s[2] ? 1 : 0);
        }
        
        Color ledEndColor = output ? PATH_ACTIVE_1_COLOR : LED_OFF_COLOR;
        FillTransition ft = new FillTransition(Duration.millis(200), outputLed, (Color)outputLed.getFill(), ledEndColor);
        ft.play();
        
        selectedIndexLabel.setText("(Selects I" + activeIndex + ")");
        
        drawPaths(numDataInputs, activeIndex, output);
    }

    private void drawPaths(int numDataInputs, int activeIndex, boolean outputValue) {
        circuitPane.getChildren().removeIf(n -> n instanceof Line);
        
        double muxStartX = 300;
        double muxWidth = 180;
        double muxHeight = 50 + numDataInputs * 50;
        double startY = 50;
        double outputY = startY + muxHeight / 2;

        Color activePathColor = dataInputs[activeIndex] ? PATH_ACTIVE_1_COLOR : PATH_ACTIVE_0_COLOR;

        // Draw Input Paths
        for (int i = 0; i < numDataInputs; i++) {
            double yPos = startY + 30 + i * 50;
            Line path = new Line(muxStartX - 30, yPos, muxStartX, yPos);
            path.setStrokeWidth(5);
            path.setStroke( (i == activeIndex) ? activePathColor : PATH_INACTIVE_COLOR );
            circuitPane.getChildren().add(path);
        }

        // Draw Active Connector Path
        double activeInputY = startY + 30 + activeIndex * 50;
        Line connector = new Line(muxStartX, activeInputY, muxStartX + muxWidth, outputY);
        connector.setStroke(activePathColor);
        connector.setStrokeWidth(5);
        circuitPane.getChildren().add(connector);
        
        // Draw Output Path
        Line outputPath = new Line(muxStartX + muxWidth, outputY, outputLed.getLayoutX() - 30, outputY);
        outputPath.setStroke(activePathColor);
        outputPath.setStrokeWidth(5);
        circuitPane.getChildren().add(outputPath);
        
        // Draw Select Lines Connection
        int numSelectInputs = selectButtons.size();
        for (int i = 0; i < numSelectInputs; i++) {
            Button btn = selectButtons.get(i);
            Line selectLine = new Line(btn.getLayoutX() + 25, btn.getLayoutY(), btn.getLayoutX() + 25, startY + muxHeight);
            selectLine.setStrokeWidth(3);
            selectLine.setStroke(selectInputs[i] ? PATH_ACTIVE_1_COLOR : PATH_INACTIVE_COLOR);
            circuitPane.getChildren().add(selectLine);
        }
    }
    
    private Label createTitleArea() {
        Label title = new Label("Dynamic Multiplexer Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        return title;
    }
    
    private HBox createModeSelector() {
        ToggleGroup group = new ToggleGroup();
        RadioButton mux4Btn = new RadioButton("4-to-1 MUX");
        RadioButton mux8Btn = new RadioButton("8-to-1 MUX");
        mux4Btn.setToggleGroup(group);
        mux8Btn.setToggleGroup(group);
        mux4Btn.setSelected(true);
        mux4Btn.setTextFill(Color.WHITE);
        mux8Btn.setTextFill(Color.WHITE);
        mux4Btn.setOnAction(e -> setMuxType(MuxType.MUX_4_TO_1));
        mux8Btn.setOnAction(e -> setMuxType(MuxType.MUX_8_TO_1));
        HBox selectorBox = new HBox(20, mux4Btn, mux8Btn);
        selectorBox.setAlignment(Pos.CENTER);
        selectorBox.setPadding(new Insets(0, 0, 10, 0));
        return selectorBox;
    }
}