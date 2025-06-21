package com.alperenulukaya.logic;

/**
 * Represents the logic of a 4-bit magnitude comparator.
 * It compares two 4-bit numbers (A and B) and determines if A > B, A < B, or A = B.
 */
public class Comparator4Bit {

    private boolean aGreaterThanB;
    private boolean aLessThanB;
    private boolean aEqualsB;

    /**
     * Compares two 4-bit binary numbers and updates the output states.
     * @param a The first 4-bit number (a[3] is MSB).
     * @param b The second 4-bit number (b[3] is MSB).
     */
    public void compare(boolean[] a, boolean[] b) {
        // Reset states before comparison
        aGreaterThanB = false;
        aLessThanB = false;
        aEqualsB = false;

        // Iterate from the most significant bit (MSB) to the LSB.
        for (int i = 3; i >= 0; i--) {
            // If the bits are different, we can determine the result.
            if (a[i] != b[i]) {
                if (a[i]) { // a[i] is 1 and b[i] is 0
                    aGreaterThanB = true;
                } else {    // a[i] is 0 and b[i] is 1
                    aLessThanB = true;
                }
                // Once a difference is found, the comparison is over.
                return;
            }
        }

        // If the loop completes without finding any differences, the numbers are equal.
        aEqualsB = true;
    }

    /**
     * Checks if A is greater than B.
     * @return true if A > B, false otherwise.
     */
    public boolean isAGreaterThanB() {
        return aGreaterThanB;
    }

    /**
     * Checks if A is less than B.
     * @return true if A < B, false otherwise.
     */
    public boolean isALessThanB() {
        return aLessThanB;
    }

    /**
     * Checks if A is equal to B.
     * @return true if A = B, false otherwise.
     */
    public boolean isAEqualsB() {
        return aEqualsB;
    }
}