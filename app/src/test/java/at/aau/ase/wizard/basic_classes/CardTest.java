package at.aau.ase.wizard.basic_classes;

import org.junit.Assert;
import org.junit.Test;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color.*;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value.*;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.*;


public class CardTest {

    private Card blueThirteen = new Card(BLUE, THIRTEEN);
    private Card redTen = new Card(RED, TEN);
    private Card yellowOne = new Card(YELLOW, ONE);
    private Card greenSeven = new Card(GREEN, SEVEN);
    private Card wizard = new Card(Color.WIZARD, Value.WIZARD);
    private Card jester = new Card(Color.JESTER, Value.JESTER);


    @Test
    public void testForGetColor() {
        Assert.assertEquals(BLUE, blueThirteen.getColor());
        Assert.assertEquals(RED, redTen.getColor());
        Assert.assertEquals(YELLOW, yellowOne.getColor());
        Assert.assertEquals(GREEN, greenSeven.getColor());
        Assert.assertEquals(Color.WIZARD, wizard.getColor());
        Assert.assertEquals(Color.JESTER, jester.getColor());

    }

    @Test
    public void testForGetValue() {
        Assert.assertEquals(THIRTEEN, blueThirteen.getValue());
        Assert.assertEquals(TEN, redTen.getValue());
        Assert.assertEquals(ONE, yellowOne.getValue());
        Assert.assertEquals(SEVEN, greenSeven.getValue());
        Assert.assertEquals(Value.WIZARD, wizard.getValue());
        Assert.assertEquals(Value.JESTER, jester.getValue());
    }

    @Test
    public void testForCardToString() {
        Assert.assertEquals("Blue Thirteen", blueThirteen.toString());
        Assert.assertEquals("Red Ten", redTen.toString());
        Assert.assertEquals("Yellow One", yellowOne.toString());
        Assert.assertEquals("Green Seven", greenSeven.toString());
        Assert.assertEquals("Wizard Wizard", wizard.toString());
        Assert.assertEquals("Jester Jester", jester.toString());
    }
}
