import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    } // Uncomment this class once you've created your Palindrome class. 

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("abcba"));
        assertFalse(palindrome.isPalindrome("aaaab"));
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));
    }

    @Test
    public void testIsOffByOnePalindrome() {
        CharacterComparator cc = new OffByOne();
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertFalse(palindrome.isPalindrome("abcba", cc));
    }

    @Test
    public void testIsOffByNPalindrome() {
        CharacterComparator cc = new OffByN(5);
        assertTrue(palindrome.isPalindrome("aaf", cc));
        assertFalse(palindrome.isPalindrome("abcba", cc));
    }
}
