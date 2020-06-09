package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


import org.junit.Assert;
import org.junit.Test;


public class PlayerTest {

    Player p1;
    Player p2;
    Player p3;



    @Test
    public void testForSettingAndGetID0() {
        p1 = new Player("Luki");
        Assert.assertEquals(0, p1.getPlayerId());
    }

    @Test
    public void testForSettingAndGetID1() {
        p2 = new Player("Andi");
        Assert.assertEquals(1, p2.getPlayerId());
    }

    @Test
    public void testForSettingAndGetID2() {
        p3 = new Player("Kevin");
        Assert.assertEquals(2, p3.getPlayerId());
    }

    @Test
    public void testForSettingAndGetNameP1() {
        p1=new Player("1");
        p1.setName("Player1");
        Assert.assertEquals("Player1", p1.getName());
    }

    @Test
    public void testForSettingAndGetNameP2() {
        p2=new Player("2");
        p2.setName("P2");
        Assert.assertEquals("P2", p2.getName());

    }

    @Test
    public void testForSettingAndGetNameP3() {
        p3 = new Player("3");
        p3.setName("Kevin3333");
        Assert.assertEquals("Kevin3333", p3.getName());
    }

}
