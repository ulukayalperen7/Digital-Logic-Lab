package com.alperenulukaya.logic;

/**
 * Represents the logic of a 3-to-8 line decoder with an Enable input.
 */
public class Decoder3to8 {

    /**
     * Calculates which output line (0-7) should be active.
     * If the enable input is false, no output is active.
     * @param enable The enable input. If false, all outputs are disabled.
     * @param a2 The most significant input bit.
     * @param a1 The middle input bit.
     * @param a0 The least significant input bit.
     * @return The integer index (0-7) of the active output, or -1 if disabled.
     */
    public int getActiveOutput(boolean enable, boolean a2, boolean a1, boolean a0) {
        if (!enable) {
            return -1; // Indicates all outputs are off
        }
        
        // Convert the 3 boolean inputs into a single integer value.
        int index = (a2 ? 4 : 0) + (a1 ? 2 : 0) + (a0 ? 1 : 0);
        return index;
    }
}