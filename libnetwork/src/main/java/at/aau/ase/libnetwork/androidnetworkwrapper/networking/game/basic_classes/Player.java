package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    static AtomicInteger nextID = new AtomicInteger();
    private int id;
    String name;
    private static short betTricks;
    private short tookTricks;
    private Integer connectionID;

    public Player() {}

    public Player(String name){
        //sets unique Player-ID starting with 0 -> equals to position in 2D Array pointsPerPlayerPerRound in Notepad
        this.id = nextID.incrementAndGet() -1;
        this.name = name;
    }

    public Player(String name, Integer connectionID) {
        this.name = name;
        this.connectionID = connectionID;
    }

    //getters + setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBetTricks(short betTricks) {
        Player.betTricks = betTricks;
    }

    public void setTookTricks(short tookTricks) {
        this.tookTricks = tookTricks;
    }

    public String getName() {
        return name;
    }

    public short getTookTricks() {
        return this.tookTricks;
    }

    public static short getBetTricks() {
        return betTricks;
    }

    public int getId() {
        return this.id;
    }
    public Integer getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(Integer connectionID) {
        this.connectionID = connectionID;
    }
}
