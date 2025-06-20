
package com.alperenulukaya.logic;

/**
 * Represents the logic of a 4-to-1 Multiplexer.
 * It selects one of the four data inputs based on the two select lines.
 * This version uses arrays for cleaner input management.
 */
public class Mux4to1 {

    /**
     * Calculates the output of the 4-to-1 multiplexer.
     * 
     * The selection logic is as follows:
     * S1 S0 | Output
     * ------|-------
     * 0  0  |  I0
     * 0  1  |  I1
     * 1  0  |  I2
     * 1  1  |  I3
     * 
     * @param dataInputs An array of 4 boolean values representing I0 to I3.
     * @param selectInputs An array of 2 boolean values representing S1 and S0.
     * @return The selected data input's value.
     */
    public boolean getOutput(boolean[] dataInputs, boolean[] selectInputs) {
        if (dataInputs == null || dataInputs.length != 4 || selectInputs == null || selectInputs.length != 2) {
            // Return a default value for invalid inputs to prevent crashes.
            return false;
        }
        
        boolean s1 = selectInputs[0];
        boolean s0 = selectInputs[1];
        
        // Convert select bits to an integer index
        int index = (s1 ? 2 : 0) + (s0 ? 1 : 0);
        
        return dataInputs[index];
    }
}