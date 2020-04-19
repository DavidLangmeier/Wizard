package at.aau.ase.wizard.basic_classes;

import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {
    @Test
    public void testForSettingAndGetID() {
        Player p1 = new Player("Luki");
        Player p2 = new Player("Andi");
        Player p3 = new Player("Kevin");

        Assert.assertEquals(0, p1.getId());
        Assert.assertEquals(1, p2.getId());
        Assert.assertEquals(2, p3.getId());
    }

    @Test
    public void testForSettingAndGetName() {
        Player p1 = new Player("Luki");
        Player p2 = new Player("Andi");
        Player p3 = new Player("Kevin");

        p3.setName("Kevin3333");

        Assert.assertEquals("Luki", p1.getName());
        Assert.assertEquals("Andi", p2.getName());
        Assert.assertEquals("Kevin3333", p3.getName());
    }


}
