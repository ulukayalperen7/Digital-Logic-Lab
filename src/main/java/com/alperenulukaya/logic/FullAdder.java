package com.alperenulukaya.logic;

/**
 * Represents the logic of a 1-bit Full Adder.
 * It adds three input bits (A, B, Carry-in) and produces a Sum and a Carry-out.
 */
public class FullAdder {

    private boolean sum;
    private boolean carryOut;

    /**
     * Updates the Sum and Carry-out based on the three input bits.
     * @param a The first input bit.
     * @param b The second input bit.
     * @param carryIn The carry-in bit.
     */
    public void update(boolean a, boolean b, boolean carryIn) {
        // The Sum is the XOR of the three inputs.
        // (A XOR B) XOR CarryIn
        this.sum = (a ^ b) ^ carryIn;
        
        // The Carry-out is true if at least two of the inputs are true.
        // (A AND B) OR (CarryIn AND (A XOR B))
        this.carryOut = (a && b) || (carryIn && (a ^ b));
    }

    /**
     * Gets the calculated Sum bit.
     * @return The Sum output.
     */
    public boolean getSum() {
        return this.sum;
    }

    /**
     * Gets the calculated Carry-out bit.
     * @return The Carry-out output.
     */
    public boolean getCarryOut() {
        return this.carryOut;
    }
}