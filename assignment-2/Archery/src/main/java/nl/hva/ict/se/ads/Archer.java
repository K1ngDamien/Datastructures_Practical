package nl.hva.ict.se.ads;
import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Holds the name, archer-id and the points scored for 30 arrows.
 * <p>
 * Archers MUST be created by using one of the generator methods. That is way the constructor is private and should stay
 * private. You are also not allowed to add any constructor with an access modifier other then private unless it is for
 * testing purposes in which case the reason why you need that constructor must be contained in a very clear manner
 * in your report.
 */
public class Archer implements Serializable {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;
    public static int MAX_SHOTS = MAX_ARROWS * MAX_ROUNDS;
    public static int HIGHEST_SCORE_PER_SHOT = 10;
    private static Random randomizer = new Random();
    private final int id; // Once assigned the id value is not allowed not change!
    private int[][] scores = new int[MAX_ROUNDS][];
    private String firstName;
    private String lastName;

    /**
     * Constructs a new instance of bowman and assigns a unique ID to the instance. The ID is not allowed to ever
     * change during the lifetime of the instance! For this you need to use the correct Java keyword.Each new instance
     * is a assigned a number that is 1 higher than the last one assigned. The first instance created should have
     * ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     * @param id
     */
    Archer(String firstName, String lastName, int id) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    /**
     * Registers the point for each of the three arrows that have been shot during a round. The <code>points</code>
     * parameter should hold the three points, one per arrow.
     *
     * @param round  the round for which to register the points.
     * @param points the points shot during the round.
     */
    public void registerScoreForRound(int round, int[] points) {
        this.scores[round] = points; // Start at index 0 for better readability
    }

    public int getTotalScore() {
        int totalScore = 0;

        for (int[] roundScore : this.scores) {
            if (roundScore != null) {
                totalScore = totalScore + IntStream.of(roundScore).sum();
            }

        }

        return totalScore;
    }

    // Concatenate all scores into scoresheet.
    public int getWeightedScore() {
        int[] allPoints = new int[MAX_SHOTS];

        int i = 0;
        for (int[] roundScore : this.scores) {
            if (roundScore != null) {
                for (int point : roundScore) {
                    allPoints[i] = point;
                    i++;
                }
            }
        }

        int [] occurrences = findCounts(allPoints);

        int totalPoints = 0;
        for (int j = 10; j >= 0; j--) {
            if (j == 0) {
                totalPoints = totalPoints - (occurrences[j] * 7);
            } else {
                int occurrence = occurrences[j];
                int value = j;
                int subsum = occurrence * (value + 1);

                totalPoints = totalPoints + subsum;
            }
        }

        return totalPoints;
    }

    public static int[] findCounts(int [] points) {
        int[] occurrences = new int[HIGHEST_SCORE_PER_SHOT + 1];

        for (int num : points) {
            occurrences[num]++;
        }

        return occurrences;
    }

    /**
     * This methods creates a List of archers.
     *
     * @param nrOfArchers the number of archers in the list.
     * @return
     */
    public static List<Archer> generateArchers(int nrOfArchers) {
        final int initialId = 135788;
        int idCounter = initialId;

        List<Archer> archers = new ArrayList<>(nrOfArchers);
        for (int i = 0; i < nrOfArchers; i++) {
            idCounter++;
            Archer archer = new Archer(Names.nextFirstName(), Names.nextSurname(), idCounter);
            letArcherShoot(archer, nrOfArchers % 100 == 0);
            archers.add(archer);
        }
        return archers;

    }

    /**
     * This methods creates a Iterator that can be used to generate all the required archers. If you implement this
     * method it is only allowed to create an instance of Archer inside the next() method!
     *
     * <b>THIS METHODS IS OPTIONAL</b>
     *
     * @param nrOfArchers the number of archers the Iterator will create.
     * @return
     */
    public static Iterator<Archer> generateArchers(long nrOfArchers) {
        return null;
    }

    public int getId() {
        return id;
    }

    private static void letArcherShoot(Archer archer, boolean isBeginner) {
        for (int round = 0; round < MAX_ROUNDS; round++) {
            archer.registerScoreForRound(round, shootArrows(isBeginner ? 0 : 1));
        }
    }

    private static int[] shootArrows(int min) {
        int[] points = new int[MAX_ARROWS];
        for (int arrow = 0; arrow < MAX_ARROWS; arrow++) {
            points[arrow] = shoot(min);
        }
        return points;
    }

    private static int shoot(int min) {
        return Math.max(min, randomizer.nextInt(11));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("%d (%d / %d) %s %s", this.getId(), this.getTotalScore(), this.getWeightedScore(), this.getFirstName(), this.getLastName());  // id totalPoints / weightedscore firstName lastName
    }
}
