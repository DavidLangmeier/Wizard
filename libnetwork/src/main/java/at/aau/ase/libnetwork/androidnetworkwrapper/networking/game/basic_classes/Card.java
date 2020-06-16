package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


import java.util.concurrent.atomic.AtomicInteger;

public class Card implements Comparable<Card>{

    private Color color;
    private Value value;
    static AtomicInteger nextID = new AtomicInteger();
    private int cardId;
    private int playedBy;

    public Card() {}

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
        this.cardId = nextID.incrementAndGet() -1;
    }

    @Override
    public int compareTo(Card card) {
        if (this.cardId < card.getCardId()) {
            return -1;
        } else if (this.cardId > card.getCardId()) {
            return 1;
        } else {
            return 0;
        }
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

    public int getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(int playedBy) {
        this.playedBy = playedBy;
    }

    public int getCardId() {
        return cardId;
    }

    public String toString() {
        String str = "";
        str += color.getColorName() + " " + value.getValueName();
        return str;
    }
}
