package com.alperenulukaya.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.alperenulukaya.logic.CpuCore;
import com.alperenulukaya.logic.CpuCore.Flag;
import com.alperenulukaya.logic.CpuCore.Register;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * A highly interactive and animated JavaFX module for visualizing a 4-bit CPU.
 * It provides a user interface for loading/editing programs, controlling
 * execution with animations, and observing the state of all CPU components.
 */
public class CPUModule {

    private final BorderPane view;
    private final CpuCore cpuCore;
    private Timeline autoRunTimeline;

    private final Map<Register, Label> registerValueLabels = new HashMap<>();
    private final Map<Flag, Circle> flagLeds = new HashMap<>();
    private final TextField[] memoryFields = new TextField[16];
    private final Label[] memoryMnemonics = new Label[16];
    private Label outputLabel;
    private Label statusLabel;
    private Button stepButton, runButton, stopButton, resetButton;
    private Slider speedSlider;

    private final String STYLE_DEFAULT = "-fx-control-inner-background: #3C3F41; -fx-text-fill: lightgreen; -fx-font-family: 'Consolas';";
    private final String STYLE_PC = "-fx-control-inner-background: #614600; -fx-text-fill: yellow; -fx-font-family: 'Consolas';";
    private final String STYLE_MAR_READ = "-fx-control-inner-background: #005050; -fx-text-fill: cyan; -fx-font-family: 'Consolas';";
    private final String STYLE_MAR_WRITE = "-fx-control-inner-background: #600030; -fx-text-fill: pink; -fx-font-family: 'Consolas';";
    private final Color LED_ON_COLOR = Color.LIME;
    private final Color LED_OFF_COLOR = Color.DARKSLATEGRAY;
    private final Color HIGHLIGHT_TEXT_COLOR = Color.YELLOW;
    private final Color DEFAULT_TEXT_COLOR = Color.CYAN;

    public CPUModule() {
        this.cpuCore = new CpuCore();
        this.view = new BorderPane();
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: #2B2B2B;");

        view.setTop(createControlPanel());
        view.setLeft(createLeftPanel());
        view.setCenter(createMemoryPanel());

        loadDefaultProgram();
        updateUI(true, -1);
    }

    private Node createControlPanel() {
        Label title = new Label("4-Bit CPU Simulator (Von Neumann)");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        stepButton = new Button("Animate Step");
        stepButton.setOnAction(e -> animateFullCycle());

        runButton = new Button("Run");
        stopButton = new Button("Stop");
        stopButton.setDisable(true);

        speedSlider = new Slider(1, 10, 4);
        Label speedLabel = new Label("Speed:");
        speedLabel.setTextFill(Color.WHITE);

        autoRunTimeline = new Timeline();
        autoRunTimeline.setOnFinished(e -> {
            if (!cpuCore.isHalted()) {
                animateFullCycle();
            }
        });

        runButton.setOnAction(e -> {
            setControlsDisabled(true);
            animateFullCycle();
        });

        stopButton.setOnAction(e -> {
            autoRunTimeline.stop();
            Node center = view.getCenter();
            if (center.getProperties().get("current_animation") instanceof Animation) {
                ((Animation) center.getProperties().get("current_animation")).stop();
            }
            setControlsDisabled(false);
            updateUI(false, -1);
        });

        resetButton = new Button("Reset CPU & Load Program");
        resetButton.setOnAction(e -> {
            autoRunTimeline.stop();
            loadDefaultProgram();
            updateUI(true, -1);
            setControlsDisabled(false);
        });

        HBox runControls = new HBox(10, runButton, stopButton, speedLabel, speedSlider);
        runControls.setAlignment(Pos.CENTER_LEFT);

        HBox mainControls = new HBox(30, stepButton, runControls, resetButton);
        mainControls.setAlignment(Pos.CENTER);
        mainControls.setPadding(new Insets(10, 0, 10, 0));

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Consolas", 14));
        statusLabel.setTextFill(Color.LIGHTSKYBLUE);
        statusLabel.setPadding(new Insets(5));
        statusLabel.setStyle("-fx-background-color: #3C3F41; -fx-background-radius: 5;");
        statusLabel.setMinWidth(600);
        statusLabel.setAlignment(Pos.CENTER);

        VBox controlBox = new VBox(10, title, mainControls, statusLabel);
        controlBox.setAlignment(Pos.CENTER);
        return controlBox;
    }

    private Node createLeftPanel() {
        GridPane registerGrid = new GridPane();
        registerGrid.setHgap(10);
        registerGrid.setVgap(10);
        Label regHeader = new Label("Registers");
        regHeader.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        regHeader.setTextFill(Color.ORANGE);
        for (int i = 0; i < Register.values().length; i++) {
            Register reg = Register.values()[i];
            Label nameLabel = new Label(reg.name());
            nameLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
            nameLabel.setTextFill(Color.LIGHTGRAY);
            Label valueLabel = new Label();
            valueLabel.setFont(Font.font("Consolas", 16));
            valueLabel.setTextFill(DEFAULT_TEXT_COLOR);
            registerGrid.add(nameLabel, 0, i);
            registerGrid.add(valueLabel, 1, i);
            registerValueLabels.put(reg, valueLabel);
        }
        VBox registerBox = new VBox(10, regHeader, registerGrid);
        registerBox.setPadding(new Insets(10));
        registerBox.setStyle("-fx-border-color: #555; -fx-border-width: 2; -fx-border-radius: 5;");

        HBox flagBox = new HBox(20);
        flagBox.setAlignment(Pos.CENTER);
        Label flagHeader = new Label("Flags:");
        flagHeader.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        flagHeader.setTextFill(Color.ORANGE);
        flagBox.getChildren().add(flagHeader);
        for (Flag flag : Flag.values()) {
            Circle led = new Circle(8, LED_OFF_COLOR);
            Label nameLabel = new Label(flag.name());
            nameLabel.setTextFill(Color.LIGHTGRAY);
            flagBox.getChildren().addAll(led, nameLabel);
            flagLeds.put(flag, led);
        }

        Label outHeader = new Label("CPU Output");
        outHeader.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        outHeader.setTextFill(Color.ORANGE);
        outputLabel = new Label(" ");
        outputLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 48));
        outputLabel.setTextFill(Color.YELLOW);
        VBox outputDisplayBox = new VBox(5, outHeader, outputLabel);
        outputDisplayBox.setAlignment(Pos.CENTER);
        outputDisplayBox.setPadding(new Insets(10));
        outputDisplayBox.setStyle("-fx-border-color: #555; -fx-border-width: 2; -fx-border-radius: 5;");

        VBox leftPanel = new VBox(20, registerBox, flagBox, outputDisplayBox);
        leftPanel.setPadding(new Insets(0, 20, 0, 10));
        return leftPanel;
    }

    private Node createMemoryPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-border-color: #555; -fx-border-width: 2; -fx-border-radius: 5;");

        Label header = new Label("Memory (16 x 8-bit) - Editable");
        header.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        header.setTextFill(Color.ORANGE);

        Pattern binaryPattern = Pattern.compile("[01]{1,8}");
        for (int i = 0; i < 16; i++) {
            final int address = i;
            Label addressLabel = new Label(String.format("0x%X:", address));
            addressLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 14));
            addressLabel.setTextFill(Color.GRAY);

            TextField valueField = new TextField();
            valueField.setFont(Font.font("Consolas", 14));
            valueField.setPrefWidth(90);

            valueField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    String text = valueField.getText().replace(" ", "");
                    if (binaryPattern.matcher(text).matches()) {
                        cpuCore.setDataInMemory(address, Integer.parseInt(text, 2));
                    }
                    updateUI(false, -1);
                }
            });

            Label mnemonicLabel = new Label();
            mnemonicLabel.setFont(Font.font("Consolas", 12));
            mnemonicLabel.setTextFill(Color.DARKGRAY);

            grid.add(addressLabel, (i % 2) * 4, i / 2);
            grid.add(valueField, (i % 2) * 4 + 1, i / 2);
            grid.add(mnemonicLabel, (i % 2) * 4 + 2, i / 2);
            memoryFields[i] = valueField;
            memoryMnemonics[i] = mnemonicLabel;
        }

        VBox memoryBox = new VBox(10, header, grid);
        memoryBox.setAlignment(Pos.TOP_CENTER);
        return memoryBox;
    }

    private void animateFullCycle() {
        if (cpuCore.isHalted()) {
            setControlsDisabled(false);
            statusLabel.setText("CPU is Halted. Press Reset to restart.");
            return;
        }
        setControlsDisabled(true);

        int pcBeforeFetch = cpuCore.getRegisterValue(Register.PC);

        SequentialTransition animation = new SequentialTransition(
                updateStatus("FETCH: Reading instruction from M[0x" + Integer.toHexString(pcBeforeFetch).toUpperCase() + "]..."),
                updateHighlight(memoryFields[pcBeforeFetch], STYLE_PC),
                pause(250),
                updateStatus("DECODE: Moving instruction to Instruction Register..."),
                updateHighlight(registerValueLabels.get(Register.IR), HIGHLIGHT_TEXT_COLOR),
                pause(250),
                update(() -> {
                    cpuCore.step();
                    updateAllRegistersAndFlags();
                }),
                pause(250),
                updateUnhighlight(registerValueLabels.get(Register.IR)),
                updateUnhighlight(memoryFields[pcBeforeFetch]),
                updateStatus("EXECUTE: " + cpuCore.getLastActionDescription()),
                pause(250),
                update(() -> {
                    int mar = cpuCore.getRegisterValue(Register.MAR);
                    int opcode = cpuCore.getRegisterValue(Register.IR) >> 4;
                    if (opcode == 0b0001 || opcode == 0b0010) {
                        memoryFields[mar].setStyle(STYLE_MAR_READ);
                    } else if (opcode == 0b0011) {
                        memoryFields[mar].setStyle(STYLE_MAR_WRITE);
                    }
                }),
                pause(400),
                update(() -> updateUI(false, pcBeforeFetch))
        );

        view.getCenter().getProperties().put("current_animation", animation);
        animation.setOnFinished(e -> {
            if (autoRunTimeline.getStatus() == Animation.Status.RUNNING) {
                double delay = 1200 / speedSlider.getValue();
                autoRunTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(delay)));
                autoRunTimeline.playFromStart();
            } else {
                setControlsDisabled(false);
            }
        });

        animation.play();
    }

    private void updateUI(boolean isReset, int lastPC) {
        if (isReset) {
            outputLabel.setText(" ");
        }
        updateAllRegistersAndFlags();

        int[] memoryState = cpuCore.getMemoryState();
        int currentPC = cpuCore.getRegisterValue(Register.PC);

        for (int i = 0; i < 16; i++) {
            memoryFields[i].setText(formatValue(memoryState[i], 8));
            memoryMnemonics[i].setText("(" + CpuCore.disassemble(memoryState[i]) + ")");
            if (i != lastPC) {
                memoryFields[i].setStyle(STYLE_DEFAULT);
            }
        }

        if (!cpuCore.isHalted()) {
            memoryFields[currentPC].setStyle(STYLE_PC);
        } else if (lastPC != -1) {
            memoryFields[lastPC].setStyle(STYLE_DEFAULT);
        }

        String output = cpuCore.getLastOutput();
        if (!output.trim().isEmpty()) {
            outputLabel.setText(output);
        }
        statusLabel.setText(cpuCore.getLastActionDescription());
    }

    private void updateAllRegistersAndFlags() {
        for (Register reg : Register.values()) {
            int value = cpuCore.getRegisterValue(reg);
            int numBits = (reg == Register.IR) ? 8 : 4;
            registerValueLabels.get(reg).setText(formatValue(value, numBits));
        }
        for (Flag flag : Flag.values()) {
            flagLeds.get(flag).setFill(cpuCore.getFlagValue(flag) ? LED_ON_COLOR : LED_OFF_COLOR);
        }
    }

    private PauseTransition pause(double millis) {
        return new PauseTransition(Duration.millis(millis / (speedSlider.getValue() / 4.0)));
    }

    private Animation updateStatus(String text) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(1));
            }

            protected void interpolate(double frac) {
                if (frac == 1.0) {
                    statusLabel.setText(text);
                }
            }
        };
    }

    private Animation update(Runnable action) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(1));
            }

            protected void interpolate(double frac) {
                if (frac == 1.0) {
                    action.run();
                }
            }
        };
    }

    private Animation updateHighlight(Node node, String style) {
        return update(() -> node.setStyle(style));
    }

    private Animation updateHighlight(Labeled node, Color color) {
        return update(() -> node.setTextFill(color));
    }

    private Animation updateUnhighlight(Node node) {
        return update(() -> {
            if (node instanceof Labeled) {
                ((Labeled) node).setTextFill(DEFAULT_TEXT_COLOR);
            }
            node.setStyle(STYLE_DEFAULT);
        });
    }

    private void setControlsDisabled(boolean disabled) {
        stepButton.setDisable(disabled);
        runButton.setDisable(disabled);
        resetButton.setDisable(disabled);
        stopButton.setDisable(!disabled);
    }

    private String formatValue(int value, int bits) {
        String padded = String.format("%" + bits + "s", Integer.toBinaryString(value)).replace(' ', '0');
        return (bits == 8) ? padded.substring(0, 4) + " " + padded.substring(4) : padded;
    }

    private void loadDefaultProgram() {
        cpuCore.reset();
        cpuCore.setDataInMemory(14, 9);
        cpuCore.setDataInMemory(15, 8);
        int[] program = {
            0b00011110, // LDA E (14)
            0b00101111, // ADD F (15) -> 9+8=17. AC=1, C=1
            0b01000000, // OUT
            0b11110000 // HLT
        };
        cpuCore.loadProgram(program, 0);
    }

    public Node getView() {
        return view;
    }

    public void stopTimeline() {
        if (autoRunTimeline != null) {
            autoRunTimeline.stop();
        }
    }
}
