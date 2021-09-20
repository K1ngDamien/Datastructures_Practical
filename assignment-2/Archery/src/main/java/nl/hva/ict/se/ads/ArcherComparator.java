package nl.hva.ict.se.ads;

import java.util.Comparator;

public class ArcherComparator implements Comparator<Archer> {

    @Override
    public int compare(Archer archer1, Archer archer2) {
        if (archer1.getTotalScore() != archer2.getTotalScore()){
            return archer1.getTotalScore() - archer2.getTotalScore();
        } else {
            if (archer1.getWeightedScore() == archer2.getWeightedScore()) {
                return archer1.getId() - archer2.getId();
            }

            return archer1.getWeightedScore() - archer2.getWeightedScore();
        }
    }
}
