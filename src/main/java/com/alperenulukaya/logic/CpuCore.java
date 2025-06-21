
package com.alperenulukaya.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the core logic of a 4-bit Von Neumann architecture CPU.
 * Manages registers, flags, memory, and instruction execution.
 */
public class CpuCore {

    public enum Register { PC, AC, IR, MAR }
    public enum Flag { Z, C } // Zero and Carry flags

    private int[] memory;
    private int pc, ac, ir, mar;
    private boolean isHalted;
    private final Map<Flag, Boolean> flags = new HashMap<>();

    private String lastOutput;
    private String lastActionDescription;

    public CpuCore() {
        this.memory = new int[16];
        reset();
    }

    /**
     * Executes a single Fetch-Decode-Execute cycle for one instruction.
     */
    public void step() {
        if (isHalted) {
            lastActionDescription = "CPU is Halted. Press Reset to restart.";
            return;
        }

        // Fetch
        mar = pc;
        ir = memory[mar];
        pc = (pc + 1) & 0x0F;

        // Decode
        int opcode = ir >> 4;
        int operand = ir & 0x0F;

        // Reset Carry flag for non-arithmetic operations
        if (opcode != 0b0010) {
            flags.put(Flag.C, false);
        }

        // Execute
        switch (opcode) {
            case 0b0001: // LDA
                mar = operand;
                ac = memory[mar] & 0x0F;
                flags.put(Flag.Z, ac == 0);
                lastActionDescription = String.format("LDA %X: Loaded M[0x%X](%d) into AC.", operand, operand, ac);
                break;

            case 0b0010: // ADD
                mar = operand;
                int dataFromMemory = memory[mar] & 0x0F;
                int oldAc = ac;
                int result = ac + dataFromMemory;
                ac = result & 0x0F; // Keep the lower 4 bits
                flags.put(Flag.Z, ac == 0);
                flags.put(Flag.C, result > 15); // Set Carry flag if result overflows
                lastActionDescription = String.format("ADD %X: Added M[0x%X](%d) to AC(%d). New AC is %d.", operand, operand, dataFromMemory, oldAc, ac);
                break;

            case 0b0011: // STA
                mar = operand;
                memory[mar] = ac;
                lastActionDescription = String.format("STA %X: Stored AC(%d) into M[0x%X].", operand, ac, operand);
                break;

            case 0b0100: // OUT
                lastOutput = String.valueOf(ac);
                lastActionDescription = String.format("OUT: Output value %d from AC.", ac);
                break;

            case 0b1111: // HLT
                isHalted = true;
                lastActionDescription = "HLT: CPU execution halted.";
                break;

            default: // NOP
                lastActionDescription = String.format("NOP: Unknown opcode %s.", Integer.toBinaryString(opcode));
                break;
        }
    }

    /**
     * Translates a machine code instruction into a human-readable mnemonic string.
     * @param instruction The 8-bit instruction.
     * @return The mnemonic string (e.g., "LDA E").
     */
    public static String disassemble(int instruction) {
        int opcode = instruction >> 4;
        int operand = instruction & 0x0F;
        switch(opcode) {
            case 0b0001: return String.format("LDA %X", operand);
            case 0b0010: return String.format("ADD %X", operand);
            case 0b0011: return String.format("STA %X", operand);
            case 0b0100: return "OUT";
            case 0b1111: return "HLT";
            default: return "NOP";
        }
    }

    /**
     * Resets the entire CPU state to its initial values.
     */
    public void reset() {
        pc = 0; ac = 0; ir = 0; mar = 0;
        isHalted = false;
        lastOutput = " ";
        flags.put(Flag.Z, true);
        flags.put(Flag.C, false);
        lastActionDescription = "CPU Reset. Ready for execution.";
        Arrays.fill(memory, 0);
    }

    public void loadProgram(int[] program, int startAddress) {
        int length = Math.min(program.length, memory.length - startAddress);
        System.arraycopy(program, 0, memory, startAddress, length);
    }

    public void setDataInMemory(int address, int value) {
        if (address >= 0 && address < memory.length) {
            memory[address] = value & 0xFF;
        }
    }

    public int getRegisterValue(Register reg) {
        switch (reg) {
            case PC: return pc;
            case AC: return ac;
            case IR: return ir;
            case MAR: return mar;
            default: return -1;
        }
    }

    public boolean getFlagValue(Flag flag) {
        return flags.getOrDefault(flag, false);
    }

    public int[] getMemoryState() {
        return Arrays.copyOf(memory, memory.length);
    }

    public boolean isHalted() {
        return isHalted;
    }

    public String getLastActionDescription() {
        return this.lastActionDescription;
    }

    public String getLastOutput() {
        String temp = lastOutput;
        lastOutput = " ";
        return temp;
    }
}