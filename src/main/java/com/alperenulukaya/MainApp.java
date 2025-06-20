// FILE: src/main/java/com/alperenulukaya/MainApp.java
package com.alperenulukaya;

import com.alperenulukaya.modules.CounterModule; // Using wildcard for cleaner imports
import com.alperenulukaya.modules.DLatchModule;
import com.alperenulukaya.modules.JKFlipFlopModule;
import com.alperenulukaya.modules.MuxModule;
import com.alperenulukaya.modules.SRLatchModule;
import com.alperenulukaya.modules.ShiftRegisterModule;
import com.alperenulukaya.modules.TFlipFlopModule;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * The main application class for the Interactive Digital Logic Lab.
 */
public class MainApp extends Application {

    private BorderPane root;
    private Stage primaryStage;
    
    // Module instances
    private CounterModule counterModule;
    private SRLatchModule srLatchModule;
    private DLatchModule dLatchModule;
    private JKFlipFlopModule jkFlipFlopModule;
    private TFlipFlopModule tFlipFlopModule;
    private ShiftRegisterModule shiftRegisterModule; // New module instance
    private MuxModule muxModule;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Interactive Digital Logic Lab");

        root = new BorderPane();
        root.setStyle("-fx-background-color: #2B2B2B;");

        VBox menu = createMenu();
        root.setLeft(menu);

        Node welcomeScreen = createWelcomeScreen();
        root.setCenter(welcomeScreen);

        Scene scene = new Scene(root, 1400, 900);
        primaryStage.setScene(scene);
        
        primaryStage.setOnCloseRequest(e -> stopAllTimelines());
        primaryStage.show();
    }

    private VBox createMenu() {
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setStyle("-fx-background-color: #3C3F41; -fx-border-color: #2B2B2B; -fx-border-width: 0 1 0 0;");
        menuBox.setPrefWidth(220);

        // Create Buttons for each module
        Button counterButton = createMenuButton("4-Bit Sync. Counter", e -> showCounterModule());
        Button shiftRegisterButton = createMenuButton("Shift Register", e -> showShiftRegisterModule()); // New button
        Button srLatchButton = createMenuButton("SR Latch", e -> showSRLatchModule());
        Button dLatchButton = createMenuButton("Gated D Latch", e -> showDLatchModule());
        Button tFlipFlopButton = createMenuButton("T Flip-Flop", e -> showTFlipFlopModule());
        Button jkFlipFlopButton = createMenuButton("JK Flip-Flop", e -> showJKFlipFlopModule());
        Button muxButton = createMenuButton("Multiplexer (MUX)", e -> showMuxModule());

        // Add buttons under their respective headers
        menuBox.getChildren().addAll(
            createMenuHeader("Applications"),
            counterButton,
            shiftRegisterButton, // Add button to menu
            createMenuHeader("Latches & Flip-Flops"),
            srLatchButton,
            dLatchButton,
            tFlipFlopButton,
            jkFlipFlopButton,
            createMenuHeader("Combinational Logic"),
            muxButton
        );

        return menuBox;
    }

    private Button createMenuButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(handler);
        return button;
    }

    private Label createMenuHeader(String text) {
        Label header = new Label(text);
        header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        header.setTextFill(Color.LIGHTGRAY);
        header.setPadding(new Insets(10, 0, 5, 0));
        return header;
    }

    private Node createWelcomeScreen() {
        Label welcomeLabel = new Label("Welcome to the Digital Logic Lab!\n\nPlease select an experiment from the menu.");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        VBox centerBox = new VBox(welcomeLabel);
        centerBox.setAlignment(Pos.CENTER);
        return centerBox;
    }
    
    private void switchModule(Node newModuleView, String title) {
        stopAllTimelines();
        root.setCenter(newModuleView);
        primaryStage.setTitle("Digital Lab - " + title);
    }

    // --- Show Module Methods ---
    private void showCounterModule() {
        if (counterModule == null) counterModule = new CounterModule();
        switchModule(counterModule.getView(), "4-Bit Counter");
    }
    
    private void showShiftRegisterModule() {
        if (shiftRegisterModule == null) shiftRegisterModule = new ShiftRegisterModule();
        switchModule(shiftRegisterModule.getView(), "Shift Register");
    }

    private void showSRLatchModule() {
        if (srLatchModule == null) srLatchModule = new SRLatchModule();
        switchModule(srLatchModule.getView(), "SR Latch");
    }

    private void showDLatchModule() {
        if (dLatchModule == null) dLatchModule = new DLatchModule();
        switchModule(dLatchModule.getView(), "D Latch");
    }

    private void showJKFlipFlopModule() {
        if (jkFlipFlopModule == null) jkFlipFlopModule = new JKFlipFlopModule();
        switchModule(jkFlipFlopModule.getView(), "JK Flip-Flop");
    }
    
    private void showTFlipFlopModule() {
        if (tFlipFlopModule == null) tFlipFlopModule = new TFlipFlopModule();
        switchModule(tFlipFlopModule.getView(), "T Flip-Flop");
    }
    
    private void showMuxModule() {
        if (muxModule == null) muxModule = new MuxModule();
        switchModule(muxModule.getView(), "Multiplexer");
    }
    
    private void stopAllTimelines() {
        if (counterModule != null) counterModule.stopTimeline();
        if (srLatchModule != null) srLatchModule.stopTimeline();
        if (dLatchModule != null) dLatchModule.stopTimeline();
        if (jkFlipFlopModule != null) jkFlipFlopModule.stopTimeline();
        if (tFlipFlopModule != null) tFlipFlopModule.stopTimeline();
        if (shiftRegisterModule != null) shiftRegisterModule.stopTimeline(); // Added stop call
        if (muxModule != null) muxModule.stopTimeline();
    }

    public static void main(String[] args) {
        launch(args);
    }
}