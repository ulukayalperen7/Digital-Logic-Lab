
package com.alperenulukaya.logic;

/**
 * Represents the logic of an 8-to-1 Multiplexer.
 * It selects one of the eight data inputs based on the three select lines.
 */
public class Mux8to1 {

    /**
     * Calculates the output of the 8-to-1 multiplexer.
     * 
     * @param dataInputs An array of 8 boolean values representing I0 to I7.
     * @param s2 The most significant select bit.
     * @param s1 The middle select bit.
     * @param s0 The least significant select bit.
     * @return The selected data input's value.
     */
    public boolean getOutput(boolean[] dataInputs, boolean s2, boolean s1, boolean s0) {
        if (dataInputs == null || dataInputs.length != 8) {
            return false; // Invalid input array
        }
        
        // Convert select bits to an integer index
        int index = (s2 ? 4 : 0) + (s1 ? 2 : 0) + (s0 ? 1 : 0);
        
        return dataInputs[index];
    }
}