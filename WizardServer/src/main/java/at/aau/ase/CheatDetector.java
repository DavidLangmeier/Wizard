package at.aau.ase;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;

/**
 * Interface for CheatDetectors
 */
public interface CheatDetector {

    /**
     * Called every time a new card is put on the table. Updates the cheating status of the player,
     * depending on the card played and the cards in this players hands and the current active color of the round.
     * @param playedCard, Card that a player wants to put on the table
     */
    public void update(Card playedCard);

    /**
     * Method used to check if a player is cheating.
     * @param playerSuspectedToCheat, String of the players name.
     * @return true if the player playerSuspectedToCheat is actually cheating
     * @return false if the player playerSuspectedToCheat is actually NOT cheating
     */
    public boolean check(String playerSuspectedToCheat);

    /**
     * Resets the cheating states of all players to false, meaning that nobody is cheating.
     * Should be called at end of round, if cheating is per-round.
     */
    public void reset();
}
