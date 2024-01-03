import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // Implement LSD Sort
        if (asciis == null || asciis.length <= 1) {
            return asciis;
        }

        int maxLength = getMaxLength(asciis);
        String[] sorted = Arrays.copyOf(asciis, asciis.length);

        for (int i = 0; i < maxLength; ++i) {
            sorted = sortHelperLSD(sorted, i, maxLength);
        }

        return sorted;
    }

    private static int getMaxLength(String[] asciis) {
        int maxLength = 0;
        for (String s : asciis) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }

    private static int asciiAtIndex(String s, int index, int maxLength) {
        if (s.length() < maxLength - index) {
            return 0;
        } 
        return (int) s.charAt(index - (maxLength - s.length()));
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] sorted, int index, int maxLength) {
        final int ASCII_RANGE = 256;

        int[] counts = new int[ASCII_RANGE];
        for (String s : sorted) {
            counts[asciiAtIndex(s, index, maxLength)]++;
        }

        int[] starts = new int[ASCII_RANGE];
        starts[0] = 0;
        for (int i = 1; i < ASCII_RANGE; ++i) {
            starts[i] = starts[i-1] + counts[i-1];
        }

        String[] sorted2 = new String[sorted.length];
        for (int i = 0; i < sorted.length; ++i) {
            int n = asciiAtIndex(sorted[i], index, maxLength);
            int place = starts[n];
            sorted2[place] = sorted[i];
            starts[n]++;
        }

        return sorted2;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String args[]) {
        String[] s = new String[5];
        s[0] = "a3e";
        s[1] = "ADFAFA";
        s[2] = "gesaf";
        s[3] = "daf";
        s[4] = "Z";

        String[] sorted = sort(s);

        for (String i : s) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (String i : sorted) {
            System.out.print(i + " ");
        }
    }
}
