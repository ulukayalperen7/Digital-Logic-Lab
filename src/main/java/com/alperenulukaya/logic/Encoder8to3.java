package com.alperenulukaya.logic;

/**
 * Represents the logic of an 8-to-3 line encoder with an Enable input.
 */
public class Encoder8to3 {

    /**
     * Converts an active input line index (0-7) into a 3-bit binary output.
     * @param enable If false, the output is all zeros regardless of input.
     * @param activeInputIndex The index of the active input line (0-7).
     * @return A 3-element boolean array representing the binary output [A2, A1, A0].
     */
    public boolean[] getOutput(boolean enable, int activeInputIndex) {
        boolean[] output = new boolean[]{false, false, false};

        if (!enable || activeInputIndex < 0 || activeInputIndex > 7) {
            return output; // Return all-zero output if disabled or no input is active
        }
        
        // A2 is the most significant bit
        output[0] = (activeInputIndex & 4) != 0; // Check if the 4's bit is set
        output[1] = (activeInputIndex & 2) != 0; // Check if the 2's bit is set
        output[2] = (activeInputIndex & 1) != 0; // Check if the 1's bit is set
        
        return output;
    }
}