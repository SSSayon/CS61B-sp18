package hw3.hash;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class HashTableVisualizer {

    public static void main(String[] args) {
        /* scale: StdDraw scale
           N:     number of items
           M:     number of buckets */

        /* After getting your simpleOomages to spread out
           nicely, be sure to try
           scale = 0.5, N = 2000, M = 100. */

        double scale = 0.5;
        // int N = 2000;
        // int M = 100;

        // HashTableDrawingUtility.setScale(scale);
        // List<Oomage> oomies = new ArrayList<>();
        // for (int i = 0; i < N; i += 1) {
        //    oomies.add(SimpleOomage.randomSimpleOomage());
        // }
        // visualize(oomies, M, scale);
    
        HashTableDrawingUtility.setScale(scale);

        List<Oomage> deadlyList = new ArrayList<>();
        ArrayList<Integer> params = new ArrayList<>(5);
        for (int i = 0; i < 5; ++i) {
            params.add(0);
        }

        for (int i = 0; i < 256; ++i) {
            params.set(0, i);
            deadlyList.add(new ComplexOomage(params));
        }

        visualize(deadlyList, 10, scale);
    }

    public static void visualize(List<Oomage> oomages, int M, double scale) {
        HashTableDrawingUtility.drawLabels(M);
        int[] numInBucket = new int[M];
        for (Oomage s : oomages) {
            int bucketNumber = (s.hashCode() & 0x7FFFFFFF) % M;
            double x = HashTableDrawingUtility.xCoord(numInBucket[bucketNumber]);
            numInBucket[bucketNumber] += 1;
            double y = HashTableDrawingUtility.yCoord(bucketNumber, M);
            s.draw(x, y, scale);
        }
    }
} 
