
package com.alperenulukaya.logic;

/**
 * Represents the logic of a Master-Slave D-type Flip-Flop.
 * It is constructed from two D-Latches.
 * The state Q only updates on the falling edge of the clock signal.
 */
public class MasterSlaveDFlipFlop {

    private final DLatch master;
    private final DLatch slave;

    public MasterSlaveDFlipFlop() {
        this.master = new DLatch();
        this.slave = new DLatch();
    }

    /**
     * Updates the flip-flop's state based on the Data and Clock inputs.
     * This method simulates the complete behavior of a clock cycle.
     * @param d The Data input.
     * @param clk The Clock input.
     */
    public void update(boolean d, boolean clk) {
        // The master latch is enabled by the clock (clk).
        // The slave latch is enabled by the inverted clock (!clk).
        master.update(d, clk);
        slave.update(master.getQ(), !clk);
    }
    
    /**
     * Resets both internal latches.
     */
    public void reset() {
        master.reset();
        slave.reset();
    }

    /**
     * Gets the output of the master latch. Useful for visualization.
     * @return The state of the master latch's Q output.
     */
    public boolean getMasterQ() {
        return master.getQ();
    }
    
    /**
     * Gets the final output of the slave latch, which is the flip-flop's output.
     * @return The state of the slave latch's Q output.
     */
    public boolean getQ() {
        return slave.getQ();
    }

    /**
     * Gets the inverted final output.
     * @return The inverted state of the slave latch's Q output.
     */
    public boolean getQNot() {
        return slave.getQNot();
    }
}