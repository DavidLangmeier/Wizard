package at.aau.ase.wizard.basic_classes;


import java.util.ArrayList;

public class Notepad {
    private static short round;
    short[][] pointsPerPlayerPerRound;

    public Notepad(short numberOfPlayers) {
        switch(numberOfPlayers){
            case 3:
                this.pointsPerPlayerPerRound = new short[3][20];
            case 4:
                this.pointsPerPlayerPerRound = new short[4][15];
            case 5:
                this.pointsPerPlayerPerRound = new short[5][12];
            case 6:
                this.pointsPerPlayerPerRound = new short[6][10];

        }
    }

    public static short getRound() {
        return round;
    }


    public ArrayList<Short> getTotalPointsPerPlayer(ArrayList<Short> pointsPerPlayerPerRound) {
        ArrayList<Short> totalPoints;

        for (short pointsPerPlayer : pointsPerPlayerPerRound) {

        }
        return pointsPerPlayerPerRound;
    }

    public static void setRound(short round) {
        Notepad.round = round;
    }
}
