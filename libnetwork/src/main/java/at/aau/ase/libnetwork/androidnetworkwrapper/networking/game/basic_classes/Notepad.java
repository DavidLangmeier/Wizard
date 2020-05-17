package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


import java.util.ArrayList;

public class Notepad {
    private static short round = 0;
    public short[][] pointsPerPlayerPerRound;
    public ArrayList<String>playerNamesList;

    //to use later
    short[][] betTricksPerPlayerPerRound;
    short[][] tookTricksPerPlayerPerRound;

    public Notepad() {}

    public Notepad(short numberOfPlayers) {
        playerNamesList=new ArrayList<String>();
        switch(numberOfPlayers){
            case 3:
                this.pointsPerPlayerPerRound = new short[3][20];
                break;
            case 4:
                this.pointsPerPlayerPerRound = new short[4][15];
                break;
            case 5:
                this.pointsPerPlayerPerRound = new short[5][12];
                break;
            case 6:
                this.pointsPerPlayerPerRound = new short[6][10];
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + numberOfPlayers);
        }
    }

    public static short getRound() {
        return round;
    }


    public short [][] getTotalPointsPerPlayer(short[][] pointsPerPlayerPerRound) {
        short [][] totalPoints = new short[pointsPerPlayerPerRound.length][1];
        for (int i = 0; i < pointsPerPlayerPerRound.length; i++) {
            for (int j = 0; j < pointsPerPlayerPerRound[i].length; j++) {
            totalPoints [i][0] +=pointsPerPlayerPerRound[i][j];
            }

        }
        return totalPoints;
    }

    public static void setRound(short round) {
        Notepad.round = round;
    }

    //brauche Notpade classe die mir werte gibt funktion
    public void testFillPointsPlayerround() {
        int count = 1;
        for (int i = 0; i < pointsPerPlayerPerRound.length; i++) {
            // playerNames[i]="name"+i;
            playerNamesList.add("name" + i);
            for (int j = 0; j < pointsPerPlayerPerRound[i].length; j++) {
                pointsPerPlayerPerRound[i][j] = (short) count;
                count--;

            }
        }
    }
    //test fill2 für switch case
        public void testFillPointsPlayerround2(){
            int count2=1;
            for(int i=0; i< pointsPerPlayerPerRound.length;i++){
                // playerNames[i]="name"+i;
                playerNamesList.add("name"+i);
                for(int j=0; j<pointsPerPlayerPerRound[i].length;j++){
                    pointsPerPlayerPerRound[i][j]= (short) count2;
                    count2++;

                }
            }

    }






}
