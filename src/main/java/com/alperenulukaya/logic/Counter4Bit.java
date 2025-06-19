package com.alperenulukaya.logic;

/**
 * Represents a 4-bit synchronous binary up/down counter made of T-Flip-Flops.
 */
public class Counter4Bit {

    public enum CountMode {
        UP, DOWN
    }

    private final T_FlipFlop[] flipFlops = new T_FlipFlop[4];
    private CountMode currentMode = CountMode.UP;

    public Counter4Bit() {
        for (int i = 0; i < 4; i++) {
            flipFlops[i] = new T_FlipFlop();
        }
    }

    public void setMode(CountMode mode) {
        this.currentMode = mode;
    }

    public CountMode getMode() {
        return this.currentMode;
    }

    public void clock() {
        boolean q0 = flipFlops[0].getQ();
        boolean q1 = flipFlops[1].getQ();
        boolean q2 = flipFlops[2].getQ();

        boolean t0, t1, t2, t3;

        if (currentMode == CountMode.UP) {
            t0 = true;
            t1 = q0;
            t2 = q0 && q1;
            t3 = q0 && q1 && q2;
        } else { // DOWN mode
            t0 = true;
            t1 = !q0;
            t2 = !q0 && !q1;
            t3 = !q0 && !q1 && !q2;
        }

        flipFlops[0].setInput(t0);
        flipFlops[1].setInput(t1);
        flipFlops[2].setInput(t2);
        flipFlops[3].setInput(t3);

        for (T_FlipFlop ff : flipFlops) {
            ff.clock();
        }
    }

    public int getValue() {
        int value = 0;
        if (flipFlops[0].getQ()) value += 1;
        if (flipFlops[1].getQ()) value += 2;
        if (flipFlops[2].getQ()) value += 4;
        if (flipFlops[3].getQ()) value += 8;
        return value;
    }

    public boolean[] getBits() {
        boolean[] bits = new boolean[4];
        for (int i = 0; i < 4; i++) {
            bits[i] = flipFlops[i].getQ();
        }
        return bits;
    }

    public void reset() {
        for (T_FlipFlop ff : flipFlops) {
            ff.reset();
        }
    }
}