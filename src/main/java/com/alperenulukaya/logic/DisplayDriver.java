package com.alperenulukaya.logic;

/**
 * A utility class that acts as a BCD to 7-Segment Display decoder.
 */
public class DisplayDriver {
    private static final boolean[][] SEGMENT_MAP = {
        //   a,      b,      c,      d,      e,      f,      g
        { true,  true,  true,  true,  true,  true,  false }, // 0
        { false, true,  true,  false, false, false, false }, // 1
        { true,  true,  false, true,  true,  false, true  }, // 2
        { true,  true,  true,  true,  false, false, true  }, // 3
        { false, true,  true,  false, false, true,  true  }, // 4
        { true,  false, true,  true,  false, true,  true  }, // 5
        { true,  false, true,  true,  true,  true,  true  }, // 6
        { true,  true,  true,  false, false, false, false }, // 7
        { true,  true,  true,  true,  true,  true,  true  }, // 8
        { true,  true,  true,  true,  false, true,  true  }, // 9
        { true,  true,  true,  false, true,  true,  true  }, // A
        { false, false, true,  true,  true,  true,  true  }, // b
        { true,  false, false, true,  true,  true,  false }, // C
        { false, true,  true,  true,  true,  false, true  }, // d
        { true,  false, false, true,  true,  true,  true  }, // E
        { true,  false, false, false, true,  true,  true  }  // F
    };

    public boolean[] getSegmentsFor(int number) {
        if (number >= 0 && number < SEGMENT_MAP.length) {
            return SEGMENT_MAP[number];
        }
        return new boolean[7]; // Blank display for invalid numbers
    }
}