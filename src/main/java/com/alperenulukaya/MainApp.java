
package com.alperenulukaya;

import com.alperenulukaya.modules.CounterModule;
import com.alperenulukaya.modules.SRLatchModule;

import javafx.application.Application;
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
 * This class sets up the main window, the menu, and handles module switching.
 */
public class MainApp extends Application {

    private BorderPane root;
    private Stage primaryStage;
    
   
    private CounterModule counterModule;
    private SRLatchModule srLatchModule;
    // Future modules will be added here

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

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        
        primaryStage.setOnCloseRequest(e -> {
            stopAllTimelines();
        });

        primaryStage.show();
    }

    private VBox createMenu() {
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setStyle("-fx-background-color: #3C3F41; -fx-border-color: #2B2B2B; -fx-border-width: 0 1 0 0;");
        menuBox.setPrefWidth(220);

        Button counterButton = new Button("4-Bit Sync. Counter");
        counterButton.setMaxWidth(Double.MAX_VALUE);
        counterButton.setOnAction(e -> showCounterModule());
        
        Button srLatchButton = new Button("SR Latch");
        srLatchButton.setMaxWidth(Double.MAX_VALUE);
        srLatchButton.setDisable(false); 
        srLatchButton.setOnAction(e -> showSRLatchModule()); 

        Button dLatchButton = new Button("D Latch & Timing (Soon)");
        dLatchButton.setMaxWidth(Double.MAX_VALUE);
        dLatchButton.setDisable(true);
        
        menuBox.getChildren().addAll(
            createMenuHeader("Applications"),
            counterButton,
            createMenuHeader("Latches & Flip-Flops"),
            srLatchButton,
            dLatchButton
        );

        return menuBox;
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
        welcomeLabel.setStyle("-fx-text-alignment: center;");

        VBox centerBox = new VBox(welcomeLabel);
        centerBox.setAlignment(Pos.CENTER);
        return centerBox;
    }
    
    private void switchModule(Node newModuleView) {
        stopAllTimelines();
        root.setCenter(newModuleView);
    }

    private void showCounterModule() {
        if (counterModule == null) {
            counterModule = new CounterModule();
        }
        switchModule(counterModule.getView());
        primaryStage.setTitle("Digital Lab - 4-Bit Counter");
    }
    
    private void showSRLatchModule() {
        if (srLatchModule == null) {
            srLatchModule = new SRLatchModule();
        }
        switchModule(srLatchModule.getView());
        primaryStage.setTitle("Digital Lab - SR Latch");
    }
    
    private void stopAllTimelines() {
        if (counterModule != null) {
            counterModule.stopTimeline();
        }
        if (srLatchModule != null) {
            srLatchModule.stopTimeline(); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}