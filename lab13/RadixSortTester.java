import java.util.Arrays;

public class RadixSortTester {
    public static void main(String[] args) {
        String[] input = generateRandomStrings(10, 5);
        System.out.println("Input: " + Arrays.toString(input));

        String[] sorted = RadixSort.sort(input);
        System.out.println("Sorted: " + Arrays.toString(sorted));
    }

    private static String[] generateRandomStrings(int size, int maxLength) {
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = generateRandomString(maxLength);
        }
        return strings;
    }

    private static String generateRandomString(int maxLength) {
        int length = (int) (Math.random() * maxLength) + 1;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) (Math.random() * 128); // Generate a random ASCII character
            sb.append(c);
        }
        return sb.toString();
    }
}