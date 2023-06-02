import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Bitmap implements Serializable {
    private static final long serialID = 1L;
    private Map<String, BitSet> index = new HashMap<>();
    private String bitmapName;

    public Bitmap(String bitmapName) {
        this.bitmapName = bitmapName;
    }

    public void add(String key, int serialId) {
        BitSet bitSet = index.getOrDefault(key, new BitSet());
        bitSet.set(serialId);
        index.put(key, bitSet);
    }

    public Bitmap processMultipleKeyQuery(Map<Bitmap, String> queries) {
        BitSet result = null;

        for(Map.Entry<Bitmap, String> query: queries.entrySet()) {
            Bitmap queryBitmap = query.getKey();    // ex. "Korea"
            BitSet bitmapIndex = queryBitmap.getIndex().get(query.getValue());  // Korea의 index를 가져온다

            if (bitmapIndex == null) {
                // 만들어둔 index가 없는 경우
                result = null;
                break;
            } else {
                if (result == null) {
                    // for문의 첫 번째 반복인 경우
                    result = bitmapIndex;
                } else {
                    BitSet bitset = (BitSet) result.clone();
                    bitset.and(bitmapIndex);
                    result = bitset;
                }
            }
        }

        if (result != null) {
            Bitmap resultBitmap = new Bitmap("Multiple key");
            resultBitmap.getIndex().put("queryResult", result);
            return resultBitmap;
        }

        return new Bitmap("Multiple key");
    }

    public int processCountQuery(String key) {
        if(index.get(key) == null) {
            return 0;
        } else {
            return index.get(key).cardinality();
        }
    }

    public String getBitmapName() { return bitmapName;  }
    public Map<String, BitSet> getIndex()   {   return index;   }
}
