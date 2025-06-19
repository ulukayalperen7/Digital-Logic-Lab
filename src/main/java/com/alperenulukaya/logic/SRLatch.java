package com.alperenulukaya.logic;

/**
 * Represents the logic of a basic SR Latch (Set-Reset Latch) using NOR gates.
 * This class holds the state (Q and Q_not) and updates it based on S and R inputs.
 */
public class SRLatch {

    private boolean q;
    private boolean qNot;

    public SRLatch() {
        reset();
    }

    /**
     * Updates the latch's state based on the Set and Reset inputs.
     * @param s The Set input (active high).
     * @param r The Reset input (active high).
     */
    public void update(boolean s, boolean r) {
        if (s && r) {
            // Invalid state for SR Latch: Both outputs go to the same state (LOW for NOR-based).
            this.q = false;
            this.qNot = false;
        } else if (s) {
            // Set state: Q is high, Q_not is low.
            this.q = true;
            this.qNot = false;
        } else if (r) {
            // Reset state: Q is low, Q_not is high.
            this.q = false;
            this.qNot = true;
        }
        // If !s && !r (memory state), do nothing. The previous state is held.
    }

    public void reset() {
        // Reset state is defined as Q=0, Q_not=1
        this.q = false;
        this.qNot = true;
    }

    public boolean getQ() {
        return this.q;
    }

    public boolean getQNot() {
        return this.qNot;
    }
}
