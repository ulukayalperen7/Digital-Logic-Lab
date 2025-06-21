package com.alperenulukaya.logic;

/**
 * Represents the logic of a 4-bit ripple-carry adder.
 * It is built by chaining four 1-bit FullAdders together.
 */
public class Adder4Bit {

    private final FullAdder[] adders = new FullAdder[4];
    private final boolean[] sum = new boolean[4];
    private boolean carryOut;

    public Adder4Bit() {
        for (int i = 0; i < 4; i++) {
            adders[i] = new FullAdder();
        }
    }

    /**
     * Updates the 4-bit sum and final carry-out based on two 4-bit inputs and an initial carry-in.
     * @param a The first 4-bit number (a[0] is the LSB).
     * @param b The second 4-bit number (b[0] is the LSB).
     * @param initialCarryIn The initial carry-in bit (for the LSB).
     */
    public void update(boolean[] a, boolean[] b, boolean initialCarryIn) {
        boolean currentCarry = initialCarryIn;

        for (int i = 0; i < 4; i++) {
            // The carry-out of the previous adder is the carry-in for the current one.
            adders[i].update(a[i], b[i], currentCarry);
            
            // Store the sum bit for this position.
            this.sum[i] = adders[i].getSum();
            
            // Update the carry for the next iteration.
            currentCarry = adders[i].getCarryOut();
        }

        // The final carry-out is the carry from the last (most significant) full adder.
        this.carryOut = currentCarry;
    }

    /**
     * Gets the 4-bit sum result.
     * @return A boolean array representing the 4-bit sum.
     */
    public boolean[] getSum() {
        return this.sum;
    }

    /**
     * Gets the final carry-out bit (C4).
     * @return The final carry-out bit.
     */
    public boolean getCarryOut() {
        return this.carryOut;
    }
}
