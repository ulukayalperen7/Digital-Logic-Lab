
package com.alperenulukaya.logic;

/**
 * Represents the logic for a 4-bit Serial-In, Serial-Out (SISO) Shift Register.
 * It is built using D-type flip-flops.
 */
public class ShiftRegister4Bit {

    // We can simulate D-FFs with a simple boolean array for this purpose.
    // bits[0] is the leftmost flip-flop (Q0), bits[3] is the rightmost (Q3).
    private final boolean[] bits = new boolean[4];
    private boolean dataIn;

    public ShiftRegister4Bit() {
        reset();
    }

    /**
     * Sets the serial data input for the next clock pulse.
     * @param data The new bit to be shifted in.
     */
    public void setDataInput(boolean data) {
        this.dataIn = data;
    }

    /**
     * Simulates a clock pulse, shifting all bits one position to the right.
     * The leftmost bit (Q0) takes the value of the serial data input.
     */
    public void clock() {
        // Shift bits from right to left in the array to simulate a right shift.
        // Q3 gets Q2's value, Q2 gets Q1's, Q1 gets Q0's.
        for (int i = 3; i > 0; i--) {
            bits[i] = bits[i - 1];
        }
        // The first flip-flop gets the new data input.
        bits[0] = this.dataIn;
    }

    /**
     * Returns the current state of all 4 bits in the register.
     * @return A boolean array representing the states of Q0, Q1, Q2, Q3.
     */
    public boolean[] getBits() {
        return this.bits;
    }
    
    /**
     * Returns the serial data output from the last flip-flop (Q3).
     * @return The value of the last bit.
     */
    public boolean getSerialOut() {
        return this.bits[3];
    }

    /**
     * Resets all bits in the register to 0.
     */
    public void reset() {
        for (int i = 0; i < 4; i++) {
            bits[i] = false;
        }
        this.dataIn = false;
    }
}