package at.aau.ase;

import org.junit.Assert;
import org.junit.Test;

public class TestEndscreenCalculations {
    EndscreenCalculations calculations = new EndscreenCalculations();

    @Test
    public void testInvert() {
        int[] array = {1,3,4,12,2};
        int[] inverted = {2,12,4,3,1};
        Assert.assertArrayEquals(inverted, calculations.invert(array));
    }
}
