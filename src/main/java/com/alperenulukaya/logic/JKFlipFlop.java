package com.alperenulukaya.logic;

/**
 * Represents the behavioral logic of a synchronous, edge-triggered JK Flip-Flop.
 * The state Q only changes on a clock pulse, based on the J and K inputs.
 */
public class JKFlipFlop {

    private boolean q; // The primary output state

    // Inputs are stored separately and are only acted upon during a clock pulse.
    private boolean jInput;
    private boolean kInput;

    public JKFlipFlop() {
        reset();
    }

    /**
     * Sets the J and K data inputs. This does not change the state.
     * The state will only change when clock() is called.
     * @param j The J input.
     * @param k The K input.
     */
    public void setInputs(boolean j, boolean k) {
        this.jInput = j;
        this.kInput = k;
    }

    /**
     * Simulates a clock pulse (e.g., a rising edge).
     * The output Q is updated based on the J and K inputs at this moment.
     */
    public void clock() {
        if (!jInput && kInput) {
            // J=0, K=1 -> Reset
            this.q = false;
        } else if (jInput && !kInput) {
            // J=1, K=0 -> Set
            this.q = true;
        } else if (jInput && kInput) {
            // J=1, K=1 -> Toggle
            this.q = !this.q;
        }
        // if !jInput && !kInput (J=0, K=0), do nothing (Hold state).
    }

    /**
     * Resets the flip-flop to its initial state (Q=0) and clears inputs.
     */
    public void reset() {
        this.q = false;
        this.jInput = false;
        this.kInput = false;
    }

    /**
     * Gets the current state of the Q output.
     * @return The value of Q.
     */
    public boolean getQ() {
        return this.q;
    }

    /**
     * Gets the current state of the inverted Q output.
     * @return The inverted value of Q.
     */
    public boolean getQNot() {
        return !this.q;
    }
}