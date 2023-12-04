public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    private static double calcFreq(int i) {
        return CONCERT_A * Math.pow(2, (i - 24) / 12);
    }

    public static void main(String[] args) {

        int n = KEYBOARD.length();
        synthesizer.GuitarString[] strings = new synthesizer.GuitarString[n];
        for (int i = 0; i < n; ++i) {
            strings[i] = new synthesizer.GuitarString(calcFreq(i));
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);
                if (index < 0) {
                    continue;
                }
                strings[index].pluck();
            }

        /* compute the superposition of samples */
            double sample = 0;
            for (synthesizer.GuitarString string : strings) {
                sample += string.sample();
            }

        /* play the sample on standard audio */
            StdAudio.play(sample);

        /* advance the simulation of each guitar string by one step */
            for (synthesizer.GuitarString string : strings) {
                string.tic();
            }
        }
    }
}

