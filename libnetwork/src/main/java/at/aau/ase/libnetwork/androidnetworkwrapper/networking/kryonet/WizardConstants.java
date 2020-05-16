package at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet;

import java.util.ArrayList;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;

public class WizardConstants {

    /**
     * Minimum numbers of payers/clients necessary to access the to gamescreen button in the lobby.
     */
    public  static final Integer MIN_NUM_PLAYERS = 3;

    /**
     * Classes to be send over the network. Since they need to be registered, just add them here to this array.
     * Also any classes, which are just used for parameters in the messages need to be listed.
     */
    private  static final Class[] wizardNetworkClasses = new Class[]{
            TextMessage.class,
            StateMessage.class,
            ActionMessage.class,
            Action.class,
            CardMessage.class,
            Card.class,
            Hand.class,
            Notepad.class,
            Player.class,
            HandMessage.class,
            PlayerMessage.class,
            LobbyMessage.class,
            ArrayList.class,
            short[][].class,
            short[].class,
            Color.class,
            Value.class,
            String[].class
    };

    private WizardConstants() {}

    public static Class[] getWizardNetworkClasses() {
        return wizardNetworkClasses;
    }
}
