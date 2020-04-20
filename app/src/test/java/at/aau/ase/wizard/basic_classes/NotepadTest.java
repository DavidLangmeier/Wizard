package at.aau.ase.wizard.basic_classes;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class NotepadTest {
    @Test
    public void testForGetRound() {
        Assert.assertEquals(0, Notepad.getRound());
    }

    @Test
    public void testForSetRound() {
        Notepad.setRound((short) 5);
        Assert.assertEquals(5, Notepad.getRound());
    }

    @Test
    public void createCorrectPointsPerPlayerPerRound() {
        Notepad np3 = new Notepad((short) 3);
        Notepad np4 = new Notepad((short) 4);
        Notepad np5 = new Notepad((short) 5);
        Notepad np6 = new Notepad((short) 6);

        short[][] ppp3 = new short[3][20];
        short[][] ppp4 = new short[4][15];
        short[][] ppp5 = new short[5][12];
        short[][] ppp6 = new short[6][10];

        Assert.assertEquals(ppp3.length, np3.pointsPerPlayerPerRound.length);
        Assert.assertEquals(ppp4.length, np4.pointsPerPlayerPerRound.length);
        Assert.assertEquals(ppp5.length, np5.pointsPerPlayerPerRound.length);
        Assert.assertEquals(ppp6.length, np6.pointsPerPlayerPerRound.length);
    }

    @Test
    public void getTotalPointsPerPlayerPerRound() {

        Notepad np3 = new Notepad((short)3);
        // sum of Points Player1 = 80
        short pointsPlayer1Round1 = 20;
        short pointsPlayer1Round2 = -10;
        short pointsPlayer1Round3 = 50;
        short pointsPlayer1Round4 = 20;

        // sum of Points Player2 = 50
        short pointsPlayer2Round1 = -10;
        short pointsPlayer2Round2 = 20;
        short pointsPlayer2Round3 = 20;
        short pointsPlayer2Round4 = 20;

        // sum of Points Player3 = 90
        short pointsPlayer3Round1 = -10;
        short pointsPlayer3Round2 = 30;
        short pointsPlayer3Round3 = 20;
        short pointsPlayer3Round4 = 50;

        // fill pointsPerPlayerPerRound
        //Player1
        np3.pointsPerPlayerPerRound[0][0] = pointsPlayer1Round1;
        np3.pointsPerPlayerPerRound[0][1] = pointsPlayer1Round2;
        np3.pointsPerPlayerPerRound[0][2] = pointsPlayer1Round3;
        np3.pointsPerPlayerPerRound[0][3] = pointsPlayer1Round4;

        //Player2
        np3.pointsPerPlayerPerRound[1][0] = pointsPlayer2Round1;
        np3.pointsPerPlayerPerRound[1][1] = pointsPlayer2Round2;
        np3.pointsPerPlayerPerRound[1][2] = pointsPlayer2Round3;
        np3.pointsPerPlayerPerRound[1][3] = pointsPlayer2Round4;

        //Player3
        np3.pointsPerPlayerPerRound[2][0] = pointsPlayer3Round1;
        np3.pointsPerPlayerPerRound[2][1] = pointsPlayer3Round2;
        np3.pointsPerPlayerPerRound[2][2] = pointsPlayer3Round3;
        np3.pointsPerPlayerPerRound[2][3] = pointsPlayer3Round4;

        //Prints out total Points per Player
        System.out.println(Arrays.deepToString(np3.getTotalPointsPerPlayer(np3.pointsPerPlayerPerRound)));

    }
}
