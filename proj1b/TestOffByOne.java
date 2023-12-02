import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars() {
        OffByOne offByOne = new OffByOne();
        assertTrue(offByOne.equalChars('&', '%'));
        assertTrue(offByOne.equalChars('a', 'b'));
        assertFalse(offByOne.equalChars('A', 'A'));
        assertFalse(offByOne.equalChars('A', 'a'));
    }
}
