import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);

        BinaryTrie trie = (BinaryTrie) or.readObject();
        int num = -1;
        try {
            num = (int) or.readObject();
        } catch (Exception e) {}

        BitSequence seq = (BitSequence) or.readObject();

        List<Character> decodedList = new ArrayList<>();
        while (seq.length() > 0) {
            Match match = trie.longestPrefixMatch(seq);
            decodedList.add(match.getSymbol());
            seq = seq.allButFirstNBits(match.getSequence().length());
        }

        char[] decodedArray = new char[decodedList.size()];
        for (int i = 0; i < decodedList.size(); ++i) {
            decodedArray[i] = decodedList.get(i);
        }
        FileUtils.writeCharArray(args[1], decodedArray);
    }
}