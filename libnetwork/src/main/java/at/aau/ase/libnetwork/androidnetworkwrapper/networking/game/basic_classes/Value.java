package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;

public enum Value {
    JESTER(0, "Jester"),
    ONE(1, "One"),
    TWO(2, "Two"),
    THREE(3, "Three"),
    FOUR(4, "Four"),
    FIVE(5, "Five"),
    SIX(6, "Six"),
    SEVEN(7, "Seven"),
    EIGHT(7, "Eight"),
    NINE(9, "Nine"),
    TEN(10, "Ten"),
    ELEVEN(11, "Eleven"),
    TWELVE(12, "Twelve"),
    THIRTEEN(13, "Thirteen"),
    WIZARD(14, "Wizard");

    private final int valueCode;
    private final String valueName;

    public int getValueCode() {
        return this.valueCode;
    }
    public String getValueName(){return this.valueName;}

    Value(int valueCode, String valueName) {
        this.valueCode = valueCode;
        this.valueName = valueName;
    }
}
