package com.alperenulukaya.logic;

/**
 * Represents a T-type (Toggle) Flip-Flop.
 * Its state (Q) toggles if the T input is high (true) on a clock pulse.
 */
public class T_FlipFlop {
    private boolean qState = false;
    private boolean tInput = false;

    public void setInput(boolean t) {
        this.tInput = t;
    }

    public void clock() {
        if (this.tInput) {
            this.qState = !this.qState;
        }
    }

    public boolean getQ() {
        return this.qState;
    }

    public void reset() {
        this.qState = false;
    }
}