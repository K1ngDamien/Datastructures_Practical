package nl.hva.ict.se.ads;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Place all your own tests for ChampionSelector in this class. Tests in any other class will be ignored!
 */
public class ExtendedChampionSelectorTest extends ChampionSelectorTest {
    @Test
    /**
     * Tests if QuickSorts gives the same expected result as CollectionSort.
     */
    public void quickSortAndCollectionSortInSameOrder() {
        List<Archer> unsortedArchersForSelIns = Archer.generateArchers(23);
        List<Archer> unsortedArchersForCollection = new ArrayList<>(unsortedArchersForSelIns);

        List<Archer> sortedArchersQuicksort = ChampionSelector.quickSort(unsortedArchersForCollection, 0, unsortedArchersForCollection.size(), comparator);
        List<Archer> sortedArchersCollection = ChampionSelector.collectionSort(unsortedArchersForCollection, comparator);

        assertEquals(sortedArchersCollection, sortedArchersQuicksort);
    }

    /**
     * Test if CollectionSort gives the same expected result as insertionSort.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void collectionSortAndInsertionSortInSameOrder() {
        List<Archer> unsortedInput = Archer.generateArchers(23);


        List<Archer> sortedInsertionSort = ChampionSelector.selInsSort(unsortedInput, comparator);
        List<Archer> sortedCollectionSort = ChampionSelector.collectionSort(unsortedInput, comparator);

        assertEquals(sortedCollectionSort, sortedInsertionSort);
    }

    @Test
    public void benchMarkTest () throws IOException, ClassNotFoundException {
        int nrOfArchers = 100;
        double insertSortTime = 0; // Stop insertion time tests when > 20 sec.
        double quickSortTime = 0;
        while (nrOfArchers <= 5000000) {
            List<Archer> testArcherList;

            // To make sure that we always run the same dataset, we first generate lists which we store as temp files and read in later. This makes sure that we can, in case we need this, run tests again over the same dataset.
            File fileStore = new File("archerList" + nrOfArchers + ".tmp");
            if (fileStore.exists()) {
                FileInputStream fis = new FileInputStream("archerList" + nrOfArchers + ".tmp");
                ObjectInputStream ois = new ObjectInputStream(fis);
                testArcherList = (List<Archer>) ois.readObject();
                ois.close();

                System.out.println(String.format("Reading existing dataset for nrArchers: %d", nrOfArchers));
            } else {
                testArcherList = Archer.generateArchers(nrOfArchers);
                FileOutputStream fos = new FileOutputStream("archerList" + nrOfArchers + ".tmp");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(testArcherList);
                oos.close();

                System.out.println(String.format("Generating permanent list of nrOfArchers: %d", nrOfArchers));
            }


            if (quickSortTime <= 20) {
                final long  quickStartTime = System.nanoTime();
                ChampionSelector.quickSort(testArcherList, 0, testArcherList.size(), new ArcherComparator());
                final long quickDuration = System.nanoTime() - quickStartTime;
                final double quickSeconds = ((double) quickDuration / 1000000000);

                quickSortTime = quickSeconds;
                System.out.format("Algorithm: Quicksort - Amount of Archers: %d - Estimated time: %f seconds. \n\n\n", nrOfArchers, quickSeconds);
            } else {
                System.out.println("Quick sort took more than 20 seconds to execute. ");
            }

            if (insertSortTime <= 20) {
                final long  insertStartTime = System.nanoTime();
                ChampionSelector.selInsSort(testArcherList, new ArcherComparator());
                final long insertDuration = System.nanoTime() - insertStartTime;
                final double insertSeconds = ((double) insertDuration / 1000000000);

                // Set insertSeconds for autostop
                insertSortTime = insertSeconds;

                System.out.format("Algorithm: Insertion Sort - Amount of Archers: %d - Estimated time: %f seconds. \n\n\n", nrOfArchers, insertSeconds);
            } else {
                System.out.println("Insertion sort time took to long!");
            }

            final long  collectStartTime = System.nanoTime();
            ChampionSelector.collectionSort(testArcherList, new ArcherComparator());
            final long collectDuration = System.nanoTime() - collectStartTime;
            final double collectSeconds = ((double) collectDuration / 1000000000);

            System.out.format("Algorithm: Collection Sort - Amount of Archers: %d - Estimated time: %f seconds. \n\n\n", nrOfArchers, collectSeconds);

            nrOfArchers = nrOfArchers * 2;
        }
    }
}
