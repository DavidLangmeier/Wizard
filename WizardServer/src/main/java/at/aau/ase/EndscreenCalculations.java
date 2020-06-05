package at.aau.ase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static com.esotericsoftware.minlog.Log.info;

public class EndscreenCalculations {


    int[] rankingIndices(int [][] totalPointsPerPlayer) {
        int[][] a = totalPointsPerPlayer;
        int[] sortedIndices = IntStream.range(0, a.length)
                .boxed().sorted(Comparator.comparingInt(i -> a[i][0]))
                .mapToInt(e -> e).toArray();
        invert(sortedIndices);
        return sortedIndices;
    }

    List sortPlayersByRanking(int[][] totalPointsPerPlayer, List<String> playerNames) {
        int[] index = rankingIndices(totalPointsPerPlayer);
        info(Arrays.toString(index));
        List<String> playersInRankingOrder = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++) {
            playersInRankingOrder.add(i, playerNames.get(index[i]));
        }
        return playersInRankingOrder;
    }

    int[][] sortPlayerTotalPointsByRanking(int[][] totalPointsPerPlayer, List<String> playerNames) {
        int[] index = rankingIndices(totalPointsPerPlayer);
        int[][] returnPPP = new int[totalPointsPerPlayer.length][1];
        for (int i = 0; i < playerNames.size(); i++) {
            returnPPP[i][0] = totalPointsPerPlayer[index[i]][0];
        }
        return returnPPP;

    }

    int[] setActualIconID(int[][] totalPointsInRankingOrder) {
        int[] actualIconID = new int[totalPointsInRankingOrder.length];
        int counter = 0;
        for (int i = 1; i < totalPointsInRankingOrder.length; i++) {
            if (totalPointsInRankingOrder[i - 1][0] == totalPointsInRankingOrder[i][0]) {
                actualIconID[i] = counter;
            } else {
                actualIconID[i] = ++counter;
            }
        }
        return actualIconID;

    }

    int [] invert(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }
}
