package com.alperenulukaya;

import com.alperenulukaya.modules.Adder4BitModule;
import com.alperenulukaya.modules.CounterModule;
import com.alperenulukaya.modules.DLatchModule;
import com.alperenulukaya.modules.DecoderModule;
import com.alperenulukaya.modules.EncoderModule;
import com.alperenulukaya.modules.FullAdderModule; // <-- 1. Import the new 4-Bit Adder module
import com.alperenulukaya.modules.JKFlipFlopModule;
import com.alperenulukaya.modules.MasterSlaveModule;
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
    private MasterSlaveModule masterSlaveModule;
    private TFlipFlopModule tFlipFlopModule;
    private JKFlipFlopModule jkFlipFlopModule;
    private ShiftRegisterModule shiftRegisterModule;
    private MuxModule muxModule;
    private DecoderModule decoderModule;
    private EncoderModule encoderModule;
    private FullAdderModule fullAdderModule;
    private Adder4BitModule adder4BitModule; // <-- 2. Declare the 4-Bit Adder module instance

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

        // Create buttons for all modules
        Button counterButton = createMenuButton("4-Bit Sync. Counter", e -> showCounterModule());
        Button shiftRegisterButton = createMenuButton("Shift Register", e -> showShiftRegisterModule());
        Button srLatchButton = createMenuButton("SR Latch", e -> showSRLatchModule());
        Button dLatchButton = createMenuButton("Clocked D Latch", e -> showDLatchModule());
        Button masterSlaveButton = createMenuButton("Master-Slave D-FF", e -> showMasterSlaveModule());
        Button tFlipFlopButton = createMenuButton("T Flip-Flop", e -> showTFlipFlopModule());
        Button jkFlipFlopButton = createMenuButton("JK Flip-Flop", e -> showJKFlipFlopModule());
        Button muxButton = createMenuButton("Multiplexer (MUX)", e -> showMuxModule());
        Button decoderButton = createMenuButton("3-to-8 Decoder", e -> showDecoderModule());
        Button encoderButton = createMenuButton("8-to-3 Encoder", e -> showEncoderModule());
        Button fullAdderButton = createMenuButton("1-Bit Full Adder", e -> showFullAdderModule());
        Button adder4BitButton = createMenuButton("4-Bit Adder", e -> showAdder4BitModule()); // <-- 3. Create the button for the new module

        // Add all components to the menu VBox
        menuBox.getChildren().addAll(
                createMenuHeader("Applications"),
                counterButton,
                shiftRegisterButton,
                createMenuHeader("Latches & Flip-Flops"),
                srLatchButton,
                dLatchButton,
                masterSlaveButton,
                tFlipFlopButton,
                jkFlipFlopButton,
                createMenuHeader("Combinational Logic"),
                muxButton,
                decoderButton,
                encoderButton,
                fullAdderButton,
                adder4BitButton // <-- 4. Add the button to the menu
        );

        return menuBox;
    }

    private Button createMenuButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(handler);
        // Add any common styling for buttons here if desired
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
        Label welcomeLabel = new Label("Welcome to the Digital Logic Lab!\n\nPlease select a circuit from the menu.");
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
        if (counterModule == null) {
            counterModule = new CounterModule();
        }
        switchModule(counterModule.getView(), "4-Bit Counter");
    }

    private void showShiftRegisterModule() {
        if (shiftRegisterModule == null) {
            shiftRegisterModule = new ShiftRegisterModule();
        }
        switchModule(shiftRegisterModule.getView(), "Shift Register");
    }

    private void showSRLatchModule() {
        if (srLatchModule == null) {
            srLatchModule = new SRLatchModule();
        }
        switchModule(srLatchModule.getView(), "SR Latch");
    }

    private void showDLatchModule() {
        if (dLatchModule == null) {
            dLatchModule = new DLatchModule();
        }
        switchModule(dLatchModule.getView(), "Clocked D Latch");
    }

    private void showMasterSlaveModule() {
        if (masterSlaveModule == null) {
            masterSlaveModule = new MasterSlaveModule();
        }
        switchModule(masterSlaveModule.getView(), "Master-Slave D-FF");
    }

    private void showJKFlipFlopModule() {
        if (jkFlipFlopModule == null) {
            jkFlipFlopModule = new JKFlipFlopModule();
        }
        switchModule(jkFlipFlopModule.getView(), "JK Flip-Flop");
    }

    private void showTFlipFlopModule() {
        if (tFlipFlopModule == null) {
            tFlipFlopModule = new TFlipFlopModule();
        }
        switchModule(tFlipFlopModule.getView(), "T Flip-Flop");
    }

    private void showMuxModule() {
        if (muxModule == null) {
            muxModule = new MuxModule();
        }
        switchModule(muxModule.getView(), "Multiplexer");
    }

    private void showDecoderModule() {
        if (decoderModule == null) {
            decoderModule = new DecoderModule();
        }
        switchModule(decoderModule.getView(), "3-to-8 Decoder");
    }

    private void showEncoderModule() {
        if (encoderModule == null) {
            encoderModule = new EncoderModule();
        }
        switchModule(encoderModule.getView(), "8-to-3 Encoder");
    }

    private void showFullAdderModule() {
        if (fullAdderModule == null) {
            fullAdderModule = new FullAdderModule();
        }
        switchModule(fullAdderModule.getView(), "1-Bit Full Adder");
    }

    private void showAdder4BitModule() { // <-- 5. Add the method to show the new module
        if (adder4BitModule == null) {
            adder4BitModule = new Adder4BitModule();
        }
        switchModule(adder4BitModule.getView(), "4-Bit Adder");
    }

    private void stopAllTimelines() {
        if (counterModule != null) {
            counterModule.stopTimeline();
        }
        if (shiftRegisterModule != null) {
            shiftRegisterModule.stopTimeline();
        }
        if (srLatchModule != null) {
            srLatchModule.stopTimeline();
        }
        if (dLatchModule != null) {
            dLatchModule.stopTimeline();
        }
        if (masterSlaveModule != null) {
            masterSlaveModule.stopTimeline();
        }
        if (jkFlipFlopModule != null) {
            jkFlipFlopModule.stopTimeline();
        }
        if (tFlipFlopModule != null) {
            tFlipFlopModule.stopTimeline();
        }
        if (muxModule != null) {
            muxModule.stopTimeline();
        }
        if (decoderModule != null) {
            decoderModule.stopTimeline();
        }
        if (encoderModule != null) {
            encoderModule.stopTimeline();
        }
        if (fullAdderModule != null) {
            fullAdderModule.stopTimeline();
        }
        if (adder4BitModule != null) {
            adder4BitModule.stopTimeline(); // <-- 6. Add the module to the cleanup method

            }}

    public static void main(String[] args) {
        launch(args);
    }
}
