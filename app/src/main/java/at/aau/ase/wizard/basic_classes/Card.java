package at.aau.ase.wizard.basic_classes;


public class Card {

    private final Color color;
    private final Value value;


    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }
//To find out the ID für Drafwable: jps combination  Color &Zero & Name
    public String getPictureFileId() {
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

    public String toString() {
        String str = "";
        str += color.getColorName() + " " + value.getValueName();
        return str;
    }
}
