package at.aau.ase;

import com.esotericsoftware.minlog.Log;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

public class CheatDetectorImpl implements CheatDetector {

    private Game game;
    private Hand[] playerHands;
    private List<Player> players;
    private boolean[] isCheating; // for all players

    public CheatDetectorImpl(Game game) {
        this.game = game;
        this.playerHands = game.getPlayerHands();
        this.players = game.getPlayers();
        this.isCheating = new boolean[this.players.size()];
    }

    public void update(Card playedCard) {
        int activePlayerIdx = playedCard.getPlayedBy();
        Hand handToCheck = playerHands[activePlayerIdx];
        Color activeColor = game.getActiveColor();

        if (playedCard.getColor() != activeColor && isNoJesterOrWizard(playedCard)) { // Jester + Wizard can always be played
            for (Card cardInHand : handToCheck.getCards()) { // Possibly cheating, check players cardsInHand
                if (cardInHand.getColor() == activeColor) { // Cheating!
                    isCheating[activePlayerIdx] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < isCheating.length; i++) {
            Log.info("=============> CHEATING: ischeating("+players.get(i).getName()+") = "+isCheating[i]);
        }
    }

    private boolean isNoJesterOrWizard(Card playedCard) {
        return playedCard.getColor() != Color.JESTER && playedCard.getColor() != Color.WIZARD;
    }

    public boolean check(String playerSuspectedToCheat) {
        boolean cheating = false;
        for (int i = 0; i < players.size(); i++) { // One of them will match
            if (players.get(i).getName().equals(playerSuspectedToCheat)) {
                cheating = isCheating[i];
                isCheating[i] = false;
                break;
            }
        }
        return cheating;
    }

    public void reset() {
        this.isCheating = new boolean[this.players.size()];
    }
}
