package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


public class Notepad {
    private static int round = 0;
    private int[][] pointsPerPlayerPerRound;

    //to use later
    private int[][] betTricksPerPlayerPerRound;
    private int[][] tookTricksPerPlayerPerRound;

    public Notepad() {
    }

    public Notepad(int numberOfPlayers) {
        this.pointsPerPlayerPerRound = new int[numberOfPlayers][60/numberOfPlayers];
        this.betTricksPerPlayerPerRound = new int[numberOfPlayers][60/numberOfPlayers];
        this.tookTricksPerPlayerPerRound = new int[numberOfPlayers][60/numberOfPlayers];
    }

    public static int getRound() {
        return round;
    }


    public int[][] getTotalPointsPerPlayer() {
        int[][] totalPoints = new int[pointsPerPlayerPerRound.length][1];
        for (int i = 0; i < pointsPerPlayerPerRound.length; i++) {
            for (int j = 0; j < pointsPerPlayerPerRound[i].length; j++) {
                totalPoints[i][0] += pointsPerPlayerPerRound[i][j];
            }

        }
        return totalPoints;
    }

    public static void setRound(int round) {
        Notepad.round = round;
    }

    public void setPointsPerPlayerPerRound(int playerID, int pointsForThisRound, int currentRound) {
        this.pointsPerPlayerPerRound[playerID][currentRound-1] = pointsForThisRound;
    }

    public int[][] getPointsPerPlayerPerRound() {
        return pointsPerPlayerPerRound;
    }

    public void setBetTricksPerPlayerPerRound(int playerID, int betTricks, int currentRound) {
        this.betTricksPerPlayerPerRound[playerID][currentRound-1] = betTricks;
    }

    public int[][] getBetTricksPerPlayerPerRound() {
        return betTricksPerPlayerPerRound;
    }

    public void setTookTricksPerPlayerPerRound(int playerID, int currentRound) {
        this.tookTricksPerPlayerPerRound[playerID][currentRound-1] +=1;
    }

    public int[][] getTookTricksPerPlayerPerRound() {
        return tookTricksPerPlayerPerRound;
    }
}
