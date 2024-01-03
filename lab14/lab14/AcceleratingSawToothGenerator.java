package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private double mul;

    public AcceleratingSawToothGenerator(int period, double mul) {
        this.period = period;
        this.mul = mul;
        state = 0;
    }

    public double next() {
        state = state + 1;
        if (state % period == 0) {
            period = (int) Math.floor(period * mul);
            state = 0;
        }
        return normalize(state);
    }

    private double normalize(int n) {
        return 2.0 * n / (period - 1) - 1;
    }
}
