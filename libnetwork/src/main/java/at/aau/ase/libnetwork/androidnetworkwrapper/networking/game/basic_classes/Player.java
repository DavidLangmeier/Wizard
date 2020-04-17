package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


public class Player {
    String name;
    private static short betTricks;
    private short tookTricks;

    public Player(String name){
        this.name = name;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBetTricks(short betTricks) {
        Player.betTricks = betTricks;
    }

    public void setTookTricks(short tookTricks) {
        this.tookTricks = tookTricks;
    }



    //getters

    public String getName() {
        return name;
    }

    public short getTookTricks() {
        return tookTricks;
    }

    public static short getBetTricks() {
        return betTricks;
    }

}
