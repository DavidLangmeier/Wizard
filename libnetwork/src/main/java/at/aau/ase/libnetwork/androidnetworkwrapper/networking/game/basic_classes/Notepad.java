package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


public class Notepad {
    private static short round = 0;
    public short[][] pointsPerPlayerPerRound;

    //to use later
    short[][] betTricksPerPlayerPerRound;
    short[][] tookTricksPerPlayerPerRound;

    public Notepad() {
    }

    public Notepad(short numberOfPlayers) {
        this.pointsPerPlayerPerRound = new short[numberOfPlayers][60/numberOfPlayers];
        this.betTricksPerPlayerPerRound = new short[numberOfPlayers][60/numberOfPlayers];
        this.tookTricksPerPlayerPerRound = new short[numberOfPlayers][60/numberOfPlayers];
    }

    public static short getRound() {
        return round;
    }


    public short[][] getTotalPointsPerPlayer() {
        short[][] totalPoints = new short[pointsPerPlayerPerRound.length][1];
        for (int i = 0; i < pointsPerPlayerPerRound.length; i++) {
            for (int j = 0; j < pointsPerPlayerPerRound[i].length; j++) {
                totalPoints[i][0] += pointsPerPlayerPerRound[i][j];
            }

        }
        return totalPoints;
    }

    public static void setRound(short round) {
        Notepad.round = round;
    }

    public void setBetTricksPerPlayerPerRound(short playerID, short betTricks) {
        this.betTricksPerPlayerPerRound[playerID][round] = betTricks;
    }

    public short[][] getBetTricksPerPlayerPerRound() {
        return betTricksPerPlayerPerRound;
    }

    public void setTookTricksPerPlayerPerRound(short[][] tookTricksPerPlayerPerRound) {
        this.tookTricksPerPlayerPerRound = tookTricksPerPlayerPerRound;
    }

    public short[][] getTookTricksPerPlayerPerRound() {
        return pointsPerPlayerPerRound;
    }
}
