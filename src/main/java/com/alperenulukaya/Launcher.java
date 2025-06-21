package com.alperenulukaya;

/**
 * A separate launcher class to work around the JavaFX fat-jar issue.
 * This class's main method will be the entry point for the packaged application.
 */
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}