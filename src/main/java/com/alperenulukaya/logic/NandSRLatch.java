
package com.alperenulukaya.logic;

/**
 * Represents the behavioral logic of an SR Latch built from NAND gates.
 * This latch is "active-low", meaning S=0 sets it and R=0 resets it.
 */
public class NandSRLatch {

    private boolean q;
    private boolean qNot;

    public NandSRLatch() {
        // Start in a stable, inactive state (S=1, R=1 -> hold)
        // Q=1, Q_not=1 is a temporary state before first set/reset.
        this.q = true;
        this.qNot = true;
    }

    /**
     * Updates the latch's state based on the active-low S and R inputs.
     * @param s The Set input (active when low).
     * @param r The Reset input (active when low).
     */
    public void update(boolean s, boolean r) {
        if (!s && !r) {
            // Invalid state for NAND Latch: Both outputs go high.
            this.q = true;
            this.qNot = true;
        } else if (!s) {
            // Set state (S=0): Q is high, Q_not is low.
            this.q = true;
            this.qNot = false;
        } else if (!r) {
            // Reset state (R=0): Q is low, Q_not is high.
            this.q = false;
            this.qNot = true;
        }
        // if s && r (S=1, R=1 memory state), do nothing. The state is held.
    }

    public void reset() {
        // Reset to the inactive hold state.
        update(true, true);
    }

    public boolean getQ() {
        return this.q;
    }

    public boolean getQNot() {
        return this.qNot;
    }
}