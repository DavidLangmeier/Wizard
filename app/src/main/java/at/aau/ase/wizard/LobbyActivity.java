package at.aau.ase.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.ErrorMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;
import static com.esotericsoftware.minlog.Log.*;

public class LobbyActivity extends AppCompatActivity {
    private Button btnStartGame;
    private static WizardClient wizardClient = null;
    private EditText etUsername = null;
    private ListView lvPlayers = null;
    private TextView tvError = null;
    private List<String> playersOnline = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter = null;
    private static Player myPlayer;
    private static GameData gameData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnStartGame = (findViewById(R.id.lobby_btn_ToGameScreen));
        btnStartGame.setOnClickListener(v -> startGame());
        btnStartGame.setEnabled(false);
        tvError = findViewById(R.id.lobby_error);
        etUsername = findViewById(R.id.lobby_edittext_username);
        etUsername.setOnKeyListener((v,keyCode,keyEvent) -> enteredUsername(keyCode,keyEvent));
        lvPlayers = findViewById(R.id.lobby_list_players);
        Integer[] icons = new Integer[]{
                R.drawable.circled1_64,
                R.drawable.circled2_64,
                R.drawable.circled3_64,
                R.drawable.circled4_64,
                R.drawable.circled5_64,
                R.drawable.circled6_64,
                R.drawable.circled7_64,
                R.drawable.circled8_64,
                R.drawable.circled9_64,
                R.drawable.circled10_64,
                R.drawable.circled11_64
        };
        arrayAdapter = new LobbyListAdapter(this, playersOnline, icons);
        lvPlayers.setAdapter(arrayAdapter);


    }

    private boolean enteredUsername(int keycode, KeyEvent keyevent) {
        if (keyevent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
            wizardClient = WizardClient.getInstance();

            wizardClient.registerCallback(basemessage -> {
                if (basemessage instanceof LobbyMessage) {
                    inCaseOfLobbyMessage((LobbyMessage) basemessage);
                }
                else if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == START)) {
                    info(basemessage.toString());
                    gameData = new GameData();
                    Intent intent = new Intent(this, GameActivity.class);
                    //intent.putExtra("myPlayer", myPlayer);
                    wizardClient.deregisterCallback();
                    startActivity(intent);
                }
                else if (basemessage instanceof PlayerMessage) {
                    PlayerMessage playerMessage = (PlayerMessage) basemessage;
                    myPlayer = playerMessage.getPlayer();
                }
                else if (basemessage instanceof ErrorMessage) {
                    ErrorMessage em = (ErrorMessage) basemessage;
                    runOnUiThread(() -> {
                        tvError.setText(em.getError());
                        etUsername.setEnabled(true);
                    });
                    wizardClient.disconnect("Ciao", myPlayer);
                }
                else {
                    error("No callback for this messagetype in the lobby: "+basemessage.toString());
                }
            });

            // wait for the connection to be fully established
            // bad solution, has to be replaced with something like server ping
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                error("Error while waiting for server callback being ready", e);
            }

            String username = etUsername.getText().toString();
            wizardClient.sendMessage(new LobbyMessage(username));
            debug(username);
            etUsername.setEnabled(false);
            return true;
        }
        return false;
    }

    private void inCaseOfLobbyMessage(LobbyMessage lobbymessage) {
        List<Player> players = lobbymessage.getPlayers();
        info("Received a LobbyMessage "+players.size() + "players online");

        runOnUiThread(() -> {
            playersOnline.clear();
            for (Player p : players) {
                playersOnline.add(p.getName());
            }
            arrayAdapter.notifyDataSetChanged();
            if (players.size() >= WizardConstants.MIN_NUM_PLAYERS && !etUsername.isEnabled()) {
                btnStartGame.setEnabled(true);
            }
        });
    }

    private void startGame() {
        wizardClient.sendMessage(new ActionMessage(START));
    }

    public static WizardClient getWizardClient() {
        return wizardClient;
    }

    public static Player getMyPlayer() {
        return myPlayer;
    }

    public static GameData getGameData() {
        return gameData;
    }
}
