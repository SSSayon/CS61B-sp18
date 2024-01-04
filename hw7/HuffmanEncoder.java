import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> ret = new HashMap<>();
        for (char s : inputSymbols) {
            if (ret.containsKey(s)) {
                ret.replace(s, ret.get(s) + 1);
            } else {
                ret.put(s, 1);
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        char[] inputSymbols = FileUtils.readFile(args[0]);

        BinaryTrie trie = new BinaryTrie(buildFrequencyTable(inputSymbols));

        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        ow.writeObject(inputSymbols.length);

        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> seqs = new ArrayList<>();

        for (char symbol : inputSymbols) {
            seqs.add(lookupTable.get(symbol));
        }

        BitSequence hugeSeq = BitSequence.assemble(seqs);
        ow.writeObject(hugeSeq);
    }
}
