package at.aau.ase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.esotericsoftware.minlog.Log.info;

public class EndscreenCalculations {



    int[] rankingIndices2(int[][] totalPointsPerPlayer) {
        int[][] localTotalPoints = new int[totalPointsPerPlayer.length][1];
        int[] sortedPoints = new int[totalPointsPerPlayer.length];
        for (int i = 0; i < totalPointsPerPlayer.length; i++) {
            sortedPoints[i] = totalPointsPerPlayer[i][0];
            localTotalPoints[i][0] = totalPointsPerPlayer[i][0];
        }
        Arrays.sort(sortedPoints);
        invert(sortedPoints);
        int[] sortedIndices = new int[totalPointsPerPlayer.length];
        for (int i = 0; i < totalPointsPerPlayer.length; i++) {
            sortedIndices[i] = findPositionOfElement(localTotalPoints, sortedPoints[i]);
            localTotalPoints[sortedIndices[i]][0] = -3;
        }

        return sortedIndices;
    }

    int findPositionOfElement(int[][] totalPointsPerPlayer, int x){
        int position = -1;
        for (int i = 0; i < totalPointsPerPlayer.length; i++) {
            if(totalPointsPerPlayer[i][0] == x){
                position = i;
                break;
            }
        }
        return position;
    }

    List<String> sortPlayersByRanking(int[][] totalPointsPerPlayer, List<String> playerNames) {
        int[] index = rankingIndices2(totalPointsPerPlayer);
        info(Arrays.toString(index));
        List<String> playersInRankingOrder = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++) {
            playersInRankingOrder.add(i, playerNames.get(index[i]));
        }
        return playersInRankingOrder;
    }

    int[][] sortPlayerTotalPointsByRanking(int[][] totalPointsPerPlayer, List<String> playerNames) {
        int[] index = rankingIndices2(totalPointsPerPlayer);
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

    int[] invert(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }
}
