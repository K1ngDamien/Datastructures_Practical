package nl.hva.ict.se.ads;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        Archer damien = new Archer("Damien", "Vermaas", 1);
        int[] damienScore = {0, 8, 10};

        damien.registerScoreForRound(1, damienScore);

        Archer timo = new Archer("Timo", "Hermsen", 2);
        int [] timoScore = {8, 5, 5, 5, 10, 10, 1};

        timo.registerScoreForRound(1, timoScore);

        System.out.println(timo.getWeightedScore());
        System.out.println(damien.getWeightedScore());

        List<Archer> archerList =  new ArrayList<>();

        archerList.add(damien);
        archerList.add(timo);

        ChampionSelector.collectionSort(archerList, new ArcherComparator());
        System.out.println("Quicksort!!!");
        ChampionSelector.quickSort(archerList, 0, archerList.size(), new ArcherComparator());
        ChampionSelector.selInsSort(archerList, new ArcherComparator());

        System.out.println("Test!");

        System.out.println(damien.toString());
    }


}
