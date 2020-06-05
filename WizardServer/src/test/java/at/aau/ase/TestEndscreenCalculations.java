package at.aau.ase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEndscreenCalculations {

    @Before
    public void init() {
        playerNames.add("Player1");
        playerNames.add("Player2");
        playerNames.add("Player3");
        playerNames.add("Player4");
        playerNames.add("Player5");
    }

    private EndscreenCalculations calculations = new EndscreenCalculations();
    private int[][] totalPoints = {{10}, {20}, {-30}, {50}, {1000}};
    private List<String> playerNames = new ArrayList<>();


    @Test
    public void testInvert() {
        int[] array = {10, 20, -30, 50, 1000};
        int[] inverted = {1000, 50, -30, 20, 10};
        Assert.assertArrayEquals(inverted, calculations.invert(array));
    }

    @Test
    public void testRankingIndices() {
        int[] sortedByRankingIndices = {4, 3, 1, 0, 2};
        Assert.assertArrayEquals(sortedByRankingIndices, calculations.rankingIndices(totalPoints));
    }

    @Test
    public void testSortPlayersByRanking() {
        List<String> sortedPlayerNames = new ArrayList<>();
        sortedPlayerNames.add("Player5");
        sortedPlayerNames.add("Player4");
        sortedPlayerNames.add("Player2");
        sortedPlayerNames.add("Player1");
        sortedPlayerNames.add("Player3");
        Assert.assertEquals(sortedPlayerNames, calculations.sortPlayersByRanking(totalPoints, playerNames));
    }

    @Test
    public void testSortPlayerTotalPointsByRanking() {
        int[][] sortedPoints = {{1000},{50},{20},{10},{-30}};
        Assert.assertArrayEquals(sortedPoints, calculations.sortPlayerTotalPointsByRanking(totalPoints, playerNames));

    }

    @Test
    public void testSetActualIconIDDifferentIDs() {
        int[] actualIconID = {0,1,2,3,4};
        Assert.assertArrayEquals(actualIconID, calculations.setActualIconID(totalPoints));
    }

    @Test
    public void testSetActualIconIDSomeEqualIds() {
        int [][] equalTotalPoints = {{-20},{30},{30},{30},{40}};
        int[] actualIconID = {0,1,1,1,2};
        Assert.assertArrayEquals(actualIconID, calculations.setActualIconID(equalTotalPoints));

    }
}
