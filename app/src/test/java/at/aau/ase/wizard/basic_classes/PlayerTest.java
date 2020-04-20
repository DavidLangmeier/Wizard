package at.aau.ase.wizard.basic_classes;

import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {
    Player p1 = new Player("Luki");
    Player p2 = new Player("Andi");
    Player p3 = new Player("Kevin");
    @Test
    public void testForSettingAndGetID0() {
        Assert.assertEquals(0, p1.getId());
    }
    @Test
    public void testForSettingAndGetID1() {
        Assert.assertEquals(1, p2.getId());
    }

    @Test
    public void testForSettingAndGetID2() {
        Assert.assertEquals(2, p3.getId());
    }

    @Test
    public void testForSettingAndGetNameP1() {
        p1.setName("Player1");
        Assert.assertEquals("Player1", p1.getName());
    }

    @Test
    public void testForSettingAndGetNameP2() {
        p2.setName("P2");
        Assert.assertEquals("P2", p2.getName());

    }
    @Test
    public void testForSettingAndGetNameP3() {
        p3.setName("Kevin3333");
        Assert.assertEquals("Kevin3333", p3.getName());
    }

    }
