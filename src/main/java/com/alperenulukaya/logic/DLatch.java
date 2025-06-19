package com.alperenulukaya.logic;

/**
 * Represents the behavioral logic of a Gated D-type Latch.
 * The latch is "transparent" when the enable signal is high,
 * and "opaque" (holds its state) when the enable signal is low.
 */
public class DLatch {

    private boolean q;      // The primary output
    private boolean qNot;   // The inverted output

    public DLatch() {
        reset();
    }

    /**
     * Updates the latch's state based on the Data and Enable inputs.
     * @param d The Data input.
     * @param enable The Enable (sometimes called Clock) input.
     */
    public void update(boolean d, boolean enable) {
        if (enable) {
            // Latch is transparent. Output Q follows input D.
            this.q = d;
            this.qNot = !d;
        }
        // If enable is false, do nothing. The state is held.
    }

    /**
     * Resets the latch to its initial state (Q=0, Q_not=1).
     */
    public void reset() {
        this.q = false;
        this.qNot = true;
    }

    /**
     * Gets the current state of the Q output.
     * @return The value of Q.
     */
    public boolean getQ() {
        return this.q;
    }

    /**
     * Gets the current state of the Q_not (inverted) output.
     * @return The value of Q_not.
     */
    public boolean getQNot() {
        return this.qNot;
    }
}
