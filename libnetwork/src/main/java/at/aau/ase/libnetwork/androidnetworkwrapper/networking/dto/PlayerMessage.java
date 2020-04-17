package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class PlayerMessage {
    private Player player;

    public PlayerMessage(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }


    /* temporary solution - better implement toString method within class Player */
    @Override
    public String toString(){
        String playerInformation = "";
        playerInformation += "Player name:\t" + player.getName() + "\n";
        playerInformation += "Bet tricks:\t" + Player.getBetTricks() + "\n";
        playerInformation += "Took tricks:\t" + player.getTookTricks() + "\n";
        return playerInformation;
    }




}
