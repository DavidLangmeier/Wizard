package at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes;


import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements Serializable {
    static AtomicInteger nextID = new AtomicInteger();
    private int player_id;
    String name;
    private Integer connectionID;

    public Player() {}

    public Player(String name){
        //sets unique Player-ID starting with 0 -> equals to position in 2D Array pointsPerPlayerPerRound in Notepad
        this.player_id = nextID.incrementAndGet() -1;
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

    public String getName() {
        return name;
    }

    public int getPlayer_id() {
        return this.player_id;
    }
    public Integer getConnectionID() {
        return connectionID;
    }
}
