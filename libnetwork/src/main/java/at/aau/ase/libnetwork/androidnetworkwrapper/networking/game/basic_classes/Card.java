package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


public class Card {

    private final Color color;
    private final Value value;


    Card(Color color, Value value){
        this.color = color;
        this.value = value;
    }


    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    public String toString() {
        String str = "";
            str += color.getColorName() + " " + value.getValueName();
        return str;
    }
}
