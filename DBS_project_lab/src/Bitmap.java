import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Bitmap {
    private Map<String, BitSet> index;
    String bitmapName;

    public Bitmap(String bitmapName) {
        this.bitmapName = bitmapName;
        this.index = new HashMap<>();
    }

    public void addKey(String key, int serialId) {
        BitSet bitSet = index.getOrDefault(key, new BitSet());
        bitSet.set(serialId);
        index.put(key, bitSet);
    }

    public ArrayList<Integer> processMultipleKeyQuery() {
        ArrayList<Integer> result = new ArrayList<>();
        return result;
    }

    public ArrayList<Integer> processCountQuery() {
        ArrayList<Integer> result = new ArrayList<>();
        return result;
    }
}
