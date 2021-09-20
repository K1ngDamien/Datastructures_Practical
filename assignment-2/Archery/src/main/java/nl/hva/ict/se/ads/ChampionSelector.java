package nl.hva.ict.se.ads;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Given a list of Archer's this class can be used to sort the list using one of three sorting algorithms.
 */
public class ChampionSelector {
    /**
     * This method uses either selection sort or insertion sort for sorting the archers.
     */
    public static List<Archer> selInsSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        int n = archers.size();
        for (int i = 1; i < n; ++i) {
            Archer key = archers.get(i);
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && scoringScheme.compare(archers.get(j), key) >= 1) {
                archers.set(j + 1, archers.get(j));
                j = j - 1;
            }
            archers.set(j + 1, key);
        }

        return archers;
    }

    private static int quickSortPartition(List<Archer> archers, int low, int high, Comparator<Archer> scoringSchema) {
        Archer pivot = archers.get(high - 1);
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high - 1; j++) {
            // If current element is smaller than the pivot
            if (scoringSchema.compare(archers.get(j), pivot) <= 0) // arr[j] < pivot
            {
                i++;

                // swap arr[i] and arr[j]
                Archer temp = archers.get(i);
                archers.set(i, archers.get(j));
                archers.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        Archer temp = archers.get(i + 1);
        archers.set(i + 1, archers.get(high - 1));
        archers.set(high -1, temp);

        return i + 1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    public static List<Archer> quickSort(List<Archer> archers, int low, int high, Comparator<Archer> scoringSchema) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = quickSortPartition(archers, low, high, scoringSchema);

            // Recursively sort elements before
            // partition and after partition
            quickSort(archers, low, pi - 1, scoringSchema);
            quickSort(archers, pi + 1, high, scoringSchema);

        }

        return archers;
    }

    /**
     * This method uses the Java collections sort algorithm for sorting the archers.
     */
    public static List<Archer> collectionSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        Collections.sort(archers, scoringScheme);

        return archers;
    }

    /**
     * This method uses quick sort for sorting the archers in such a way that it is able to cope with an Iterator.
     *
     * <b>THIS METHOD IS OPTIONAL</b>
     */
    public static Iterator<Archer> quickSort(Iterator<Archer> archers, Comparator<Archer> scoringScheme) {
        return null;
    }

}
