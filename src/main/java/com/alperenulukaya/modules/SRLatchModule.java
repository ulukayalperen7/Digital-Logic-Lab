package com.alperenulukaya.modules;

import java.util.Map;

import com.alperenulukaya.logic.NandSRLatch;
import com.alperenulukaya.logic.SRLatch;
import com.alperenulukaya.util.TimingDiagram;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
 * A comprehensive UI module for demonstrating both NOR-based and NAND-based SR
 * Latches. Features toggleable inputs, a dynamic characteristic table, and a
 * timing diagram.
 */
public class SRLatchModule {

    private enum LatchType {
        NOR, NAND
    }
    private LatchType currentLatchType = LatchType.NOR;

    private final VBox view;
    private final SRLatch norLatch = new SRLatch();
    private final NandSRLatch nandLatch = new NandSRLatch();

    // UI Elements
    private Circle qLed, qNotLed;
    private Button sButton, rButton;
    private GridPane truthTable;
    private Label tableTitle;
    private Label[][] tableCellLabels = new Label[4][4];
    private TimingDiagram timingDiagram;

    private boolean sInput = false;
    private boolean rInput = false;

    // UI Constants
    private final String ACTIVE_STYLE = "-fx-background-color: #4CAF50; -fx-font-size: 18px; -fx-text-fill: white;";
    private final String INACTIVE_STYLE = "-fx-background-color: #555555; -fx-font-size: 18px; -fx-text-fill: white;";
    private final String HIGHLIGHT_STYLE = "-fx-background-color: #0066CC;";
    private final String INVALID_HIGHLIGHT_STYLE = "-fx-background-color: #A62424;";
    private final String TRANSPARENT_BG = "-fx-background-color: transparent;";

    public SRLatchModule() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_CENTER);

        Label title = createTitleArea();
        HBox modeSelector = createModeSelector();
        HBox interactionArea = createInteractionArea();

        view.getChildren().addAll(title, modeSelector, interactionArea);

        Button resetButton = new Button("Reset Latch & Diagram");
        resetButton.setFont(Font.font(16));
        resetButton.setOnAction(e -> resetAll());
        view.getChildren().add(resetButton);

        updateLatchTypeUI();
        updateLogicAndUI();
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        /* No timeline */ }

    private Label createTitleArea() {
        Label title = new Label("SR Latch Simulator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        return title;
    }

    private HBox createModeSelector() {
        ToggleGroup group = new ToggleGroup();
        RadioButton norButton = new RadioButton("NOR-based (Active-High)");
        RadioButton nandButton = new RadioButton("NAND-based (Active-Low)");

        norButton.setToggleGroup(group);
        nandButton.setToggleGroup(group);
        norButton.setSelected(true);

        norButton.setTextFill(Color.WHITE);
        nandButton.setTextFill(Color.WHITE);

        norButton.setOnAction(e -> setLatchType(LatchType.NOR));
        nandButton.setOnAction(e -> setLatchType(LatchType.NAND));

        HBox selectorBox = new HBox(20, norButton, nandButton);
        selectorBox.setAlignment(Pos.CENTER);
        selectorBox.setPadding(new Insets(0, 0, 20, 0));
        return selectorBox;
    }

    private HBox createInteractionArea() {
        VBox leftPanel = new VBox(30, createInputControls(), createCircuitDiagram());
        leftPanel.setAlignment(Pos.CENTER);

        VBox rightPanel = createRightPanel();

        return new HBox(50, leftPanel, rightPanel);
    }

    private VBox createRightPanel() {
        HBox outputs = createOutputDisplays();
        VBox table = createTruthTable();

        timingDiagram = new TimingDiagram(500, "S", "R", "Q");

        VBox rightPanel = new VBox(30, outputs, table, timingDiagram.getCanvas());
        rightPanel.setAlignment(Pos.CENTER);
        return rightPanel;
    }

    private VBox createInputControls() {
        sButton = new Button("S=0");
        rButton = new Button("R=0");
        sButton.setPrefWidth(120);
        rButton.setPrefWidth(120);

        sButton.setOnAction(e -> {
            sInput = !sInput;
            updateLogicAndUI();
        });
        rButton.setOnAction(e -> {
            rInput = !rInput;
            updateLogicAndUI();
        });

        VBox box = new VBox(20, sButton, rButton);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox createOutputDisplays() {
        qLed = new Circle(30, Color.DARKSLATEGRAY);
        qNotLed = new Circle(30, Color.DARKSLATEGRAY);
        qLed.setStroke(Color.BLACK);
        qNotLed.setStroke(Color.BLACK);

        VBox qBox = createLabeledLed("Q", qLed);
        VBox qNotBox = createLabeledLed("Q'", qNotLed);

        return new HBox(40, qBox, qNotBox);
    }

    private VBox createLabeledLed(String labelText, Circle led) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextFill(Color.WHITE);
        VBox box = new VBox(10, label, led);
        box.setAlignment(Pos.CENTER);
        return box;
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

        for (Line line : new Line[]{sLine, rLine, qLine, qNotLine}) {
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(3);
        }

        Text sText = new Text(-20, 80, "S");
        sText.setFont(Font.font(20));
        sText.setFill(Color.WHITE);
        Text rText = new Text(-20, 130, "R");
        rText.setFont(Font.font(20));
        rText.setFill(Color.WHITE);

        pane.getChildren().addAll(body, blockLabel, sLine, rLine, qLine, qNotLine, sText, rText);
        return pane;
    }

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

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tableCellLabels[i][j] = new Label();
                tableCellLabels[i][j].setFont(Font.font("Consolas", 14));
                tableCellLabels[i][j].setTextFill(Color.WHITE);
                tableCellLabels[i][j].setPrefWidth(60);
                tableCellLabels[i][j].setAlignment(Pos.CENTER);
                truthTable.add(tableCellLabels[i][j], j, i + 1);
            }
        }

        tableTitle = new Label();
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableTitle.setTextFill(Color.WHITE);

        return new VBox(10, tableTitle, truthTable);
    }

    // --- Logic and UI Update ---
    private void setLatchType(LatchType type) {
        this.currentLatchType = type;
        resetAll();
    }

    private void resetAll() {
        sInput = false;
        rInput = false;
        norLatch.reset();
        nandLatch.reset();
        timingDiagram.clear();
        updateLatchTypeUI();
        updateLogicAndUI();
    }

    private void updateLatchTypeUI() {
        String[][] norData = {
            {"0", "0", "Q(t)", "Hold"}, {"0", "1", "0", "Reset"},
            {"1", "0", "1", "Set"}, {"1", "1", "0", "Invalid"}
        };
        String[][] nandData = {
            {"0", "0", "1", "Invalid"}, {"0", "1", "1", "Set"},
            {"1", "0", "0", "Reset"}, {"1", "1", "Q(t)", "Hold"}
        };

        String[][] data = (currentLatchType == LatchType.NOR) ? norData : nandData;
        tableTitle.setText((currentLatchType == LatchType.NOR) ? "NOR-based Table" : "NAND-based Table");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tableCellLabels[i][j].setText(data[i][j]);
            }
        }
    }

    private void updateLogicAndUI() {
        sButton.setText("S=" + (sInput ? "1" : "0"));
        sButton.setStyle(sInput ? ACTIVE_STYLE : INACTIVE_STYLE);
        rButton.setText("R=" + (rInput ? "1" : "0"));
        rButton.setStyle(rInput ? ACTIVE_STYLE : INACTIVE_STYLE);

        boolean currentQ;
        if (currentLatchType == LatchType.NOR) {
            norLatch.update(sInput, rInput);
            currentQ = norLatch.getQ();
            qLed.setFill(currentQ ? Color.LIMEGREEN : Color.DARKSLATEGRAY);
            qNotLed.setFill(norLatch.getQNot() ? Color.LIMEGREEN : Color.DARKSLATEGRAY);
        } else { // NAND
            nandLatch.update(sInput, rInput);
            currentQ = nandLatch.getQ();
            qLed.setFill(currentQ ? Color.LIMEGREEN : Color.DARKSLATEGRAY);
            qNotLed.setFill(nandLatch.getQNot() ? Color.LIMEGREEN : Color.DARKSLATEGRAY);
        }

        timingDiagram.addState(Map.of("S", sInput, "R", rInput, "Q", currentQ));

        highlightTableRow();
    }

    private void highlightTableRow() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tableCellLabels[i][j].setStyle(TRANSPARENT_BG);
            }
        }

        int rowIndex = (sInput ? 2 : 0) + (rInput ? 1 : 0);

        boolean isInvalid = (currentLatchType == LatchType.NOR && sInput && rInput)
                || (currentLatchType == LatchType.NAND && !sInput && !rInput);

        String styleToApply = isInvalid ? INVALID_HIGHLIGHT_STYLE : HIGHLIGHT_STYLE;

        for (int j = 0; j < 4; j++) {
            tableCellLabels[rowIndex][j].setStyle(styleToApply);
        }
    }
}
