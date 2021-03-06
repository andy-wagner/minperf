package org.minperf.hem.recsplit;

import org.minperf.BitBuffer;
import org.minperf.hash.Mix;

public class Builder {

    private static final int[] SHIFT = { 0, 0, 0, 1, 3, 4, 5, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3 };

    private int averageBucketSize = 16;
    private int leafSize = 5;

    public Builder leafSize(int leafSize) {
        this.leafSize = leafSize;
        return this;
    }

    public Builder averageBucketSize(int averageBucketSize) {
        this.averageBucketSize = averageBucketSize;
        return this;
    }

    public BitBuffer generate(long[] keys) {
        return new FastGenerator(leafSize, averageBucketSize).generate(keys);
    }

    public BitBuffer generate(long[] keys, int len) {
        return new FastGenerator(leafSize, averageBucketSize).generate(keys, len);
    }

    public FastEvaluator evaluator(BitBuffer buff) {
        return new FastEvaluator(buff, averageBucketSize, leafSize, 0);
    }

    public static int supplementalHash(long x, int index) {
            // TODO can save one multiplication for generation
            //System.out.println("   " + x + " [" + index + "]");
            return Mix.supplementalHashWeyl(x, index);
        }

    public static int reduce(int hash, int n) {
        // http://lemire.me/blog/2016/06/27/a-fast-alternative-to-the-modulo-reduction/
        return (int) (((hash & 0xffffffffL) * n) >>> 32);
    }

    public static int getBucketCount(long size, int averageBucketSize) {
        int bucketCount = (int) ((size + averageBucketSize - 1) / averageBucketSize);
        return nextPowerOf2(bucketCount);
    }

    static int nextPowerOf2(int x) {
        if (Integer.bitCount(x) == 1) {
            return x;
        }
        return Integer.highestOneBit(x) * 2;
    }

    public static int getGolombRiceShift(int size) {
        return SHIFT[size];
    }

}
