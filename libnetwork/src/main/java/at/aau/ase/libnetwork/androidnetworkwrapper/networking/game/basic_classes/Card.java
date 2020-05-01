package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


import java.util.concurrent.atomic.AtomicInteger;

public class Card {

    private final Color color;
    private final Value value;
    static AtomicInteger nextID = new AtomicInteger();
    private int card_id;

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
        this.card_id = nextID.incrementAndGet() -1;
    }
//To find out the ID für Drafwable: jps combination  Color &Zero & Name
    public String getPictureFileId(){
        String fileIDPicture="";
        fileIDPicture=color.getColorName().toLowerCase()+"0"+value.getValueName().toLowerCase();
        return fileIDPicture;
    }

    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    public int getCard_id() {
        return card_id;
    }

    public String toString() {
        String str = "";
        str += color.getColorName() + " " + value.getValueName();
        return str;
    }
}
