package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int state;
    private int period;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }

    public double next() {
        state = state + 1;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState % period);
    }

    private double normalize(int n) {
        return 2.0 * n / (period - 1) - 1;
    }
}
