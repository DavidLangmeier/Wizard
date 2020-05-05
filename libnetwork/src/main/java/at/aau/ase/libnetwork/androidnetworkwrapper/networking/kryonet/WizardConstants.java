package at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet;

import java.util.ArrayList;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotepadMessage;
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
    public  static final Integer MIN_NUM_PLAYERS = 3;

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
            NotepadMessage.class,
            ArrayList.class,
            short[][].class,
            short[].class,
            Color.class,
            Value.class
    };

    private WizardConstants() {}

    public static Class[] getWizardNetworkClasses() {
        return wizardNetworkClasses;
    }
}
