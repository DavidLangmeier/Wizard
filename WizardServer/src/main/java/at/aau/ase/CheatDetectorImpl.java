package at.aau.ase;

import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class CheatDetectorImpl implements CheatDetector {

    private Game game;
    private Hand[] playerHands;
    private Integer activePlayerIdx;
    //private Hand table;
    //private Color trump;
    private List<Player> players;
    private boolean[] isCheating; // for all players

    public CheatDetectorImpl(Game game) {
        this.game = game;
        this.playerHands = game.getPlayerHands();
        this.activePlayerIdx = game.getActivePlayerIndex();
        //this.table = game.getTable();
        //this.trump = game.getTrump();
        this.players = game.getPlayers();
        this.isCheating = new boolean[this.players.size()];
    }

    public void update(Card playedCard) {
        activePlayerIdx = game.getActivePlayerIndex();
        Hand handToCheck = playerHands[activePlayerIdx];
        Color activeColor = game.getActiveColor();

        if (playedCard.getColor() != activeColor) { // Possibly cheating, check players cardsInHand
            for (Card cardInHand : handToCheck.getCards()) {
                if (cardInHand.getColor() == activeColor) { // Cheating!
                    isCheating[activePlayerIdx] = true;
                    break;
                }
            }
        }
        for (boolean b: isCheating) {
            Log.info("ischeating = "+b);
            Log.info("activeplayeridx = "+ (activePlayerIdx));
        }
    }

    public boolean check(String playerSuspectedToCheat) {
        int playerToCheck = 0;
        for (int i = 0; i < players.size(); i++) { // One of them will match
            if (players.get(i).getName().equals(playerSuspectedToCheat)) {
                playerToCheck = i;
                break;
            }
        }
        return isCheating[playerToCheck];
    }

    public void reset() {
        this.isCheating = new boolean[this.players.size()];
    }
}
