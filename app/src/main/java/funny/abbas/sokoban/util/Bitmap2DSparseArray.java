package funny.abbas.sokoban.util;

import android.util.Pair;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Bitmap2DSparseArray {
    private final SparseArray<long[]> bitmaps = new SparseArray<>();
    private static final int BITS_PER_LONG = 64;

    public void set(int x, int y, boolean value) {
        int index = y / BITS_PER_LONG;
        int bit = y % BITS_PER_LONG;

        long[] row = bitmaps.get(x);
        if (row == null) {
            // 估算需要的long数量：y最大值/64 + 1
            int size = (y / BITS_PER_LONG) + 1;
            row = new long[size];
            bitmaps.put(x, row);
        }

        if (index >= row.length) {
            // 扩容
            long[] newRow = new long[index + 1];
            System.arraycopy(row, 0, newRow, 0, row.length);
            row = newRow;
            bitmaps.put(x, row);
        }

        if (value) {
            row[index] |= (1L << bit);  // 设置位为1
        } else {
            row[index] &= ~(1L << bit); // 清除位为0
        }
    }

    public boolean get(int x, int y) {
        long[] row = bitmaps.get(x);
        if (row == null) return false;

        int index = y / BITS_PER_LONG;
        int bit = y % BITS_PER_LONG;

        if (index >= row.length) return false;
        return ((row[index] >> bit) & 1L) == 1L;
    }

    // 获取所有为true的坐标对
    public List<Pair<Integer, Integer>> getAllTruePositions() {
        List<Pair<Integer, Integer>> positions = new ArrayList<>();

        // 遍历所有行
        for (int i = 0; i < bitmaps.size(); i++) {
            int x = bitmaps.keyAt(i);
            long[] rowBits = bitmaps.valueAt(i);

            // 遍历该行的每个long
            for (int longIndex = 0; longIndex < rowBits.length; longIndex++) {
                long bits = rowBits[longIndex];
                if (bits == 0) continue; // 整个long都是0，跳过

                // 遍历long中的每个bit
                for (int bitPos = 0; bitPos < BITS_PER_LONG; bitPos++) {
                    if ((bits >> bitPos & 1L) == 1L) {
                        int y = longIndex * BITS_PER_LONG + bitPos;
                        positions.add(new Pair<>(x, y));
                    }
                }
            }
        }

        return positions;
    }
}