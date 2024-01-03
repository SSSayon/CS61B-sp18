package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int state;
    private int period;

    public SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }

    public double next() {
        state = state + 1;
        return normalize(state % period);
    }

    private double normalize(int n) {
        return 2.0 * n / (period - 1) - 1;
    }
}
