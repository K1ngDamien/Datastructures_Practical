package nl.hva.ict.se.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Place all your own tests for Archer in this class. Tests in any other class will be ignored!
 */
public class ExtendedArcherTest extends ArcherTest {
    Archer testArcher = new Archer("Test", "Archer", 1000);

    @BeforeEach
    public void createHardCodedArcherScores () {

        // 10 rounds are played, but we just reuse the first 5 again.
        int[] round1 = {0, 10, 5};
        int[] round2 = {5, 8, 4};
        int[] round3 = {3, 7, 4};
        int[] round4 = {9, 10, 4};
        int[] round5 = {1, 1, 0};

        testArcher.registerScoreForRound(0, round1);
        testArcher.registerScoreForRound(1, round2);
        testArcher.registerScoreForRound(2, round3);
        testArcher.registerScoreForRound(3, round4);
        testArcher.registerScoreForRound(4, round5);
        testArcher.registerScoreForRound(5, round3);
        testArcher.registerScoreForRound(6, round2);
        testArcher.registerScoreForRound(7, round1);
        testArcher.registerScoreForRound(8, round1);
        testArcher.registerScoreForRound(9, round5);

    }


    @Test
    public void testWeightedScoreCalculation() {
        assertEquals(124, testArcher.getWeightedScore());
    }

    @Test
    public void testTotalScoreCalculation () {
        assertEquals(134, testArcher.getTotalScore());
    }

    @Test
    public void testToString() {
       assertEquals("1000 (134 / 124) Test Archer", testArcher.toString());
    }
}
