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

    public void setBetTricksPerPlayerPerRound(short playerID, short betTricks, int currentRound) {
        this.betTricksPerPlayerPerRound[playerID][currentRound-1] = betTricks;
    }

    public short[][] getBetTricksPerPlayerPerRound() {
        return betTricksPerPlayerPerRound;
    }

    public void setTookTricksPerPlayerPerRound(short playerID, int currentRound) {
        this.tookTricksPerPlayerPerRound[playerID][currentRound-1] +=1;
    }

    public short[][] getTookTricksPerPlayerPerRound() {
        return tookTricksPerPlayerPerRound;
    }
}
