package at.aau.ase.wizard.basic_classes;

public enum Color {
    JESTER(0,"Jester"),
    BLUE(1, "Blue"),
    GREEN(2, "Green"),
    RED(3, "Red"),
    YELLOW(4, "Yellow"),
    WIZARD(14,"Wizard");

    private final int colorCode;
    private final String colorName;

    public int getColorCode() {
        return this.colorCode;
    }
    public String getColorName(){ return this.colorName;}

    Color(int colorCode, String colorName) {
        this.colorCode = colorCode;
        this.colorName = colorName;
    }
}
