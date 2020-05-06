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
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LobbyMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.PlayerMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.kryonet.WizardConstants;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;
import static com.esotericsoftware.minlog.Log.*;

public class LobbyActivity extends AppCompatActivity {
    private Button btnTestServer;
    private Button btnStartGame;
    private Button btnConnectToServer;
    private static WizardClient wizardClient = null;
    private TextView tvServerResponse = null;
    private EditText etUsername = null;
    private ListView lvPlayers = null;
    private List<String> players = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter = null;
    private static Player myPlayer;
    private static GameData gameData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnTestServer = findViewById(R.id.lobby_btn_testServer);
        btnTestServer.setOnClickListener(v -> testServer());
        btnStartGame = (findViewById(R.id.lobby_btn_ToGameScreen));
        btnStartGame.setOnClickListener(v -> startGame());
        btnStartGame.setEnabled(false);
        btnConnectToServer = findViewById(R.id.lobby_btn_connect);
        btnConnectToServer.setOnClickListener(v -> connectToServer());
        tvServerResponse = findViewById(R.id.lobby_text_serverResponseDisplay);
        etUsername = findViewById(R.id.lobby_edittext_username);
        //etUsername.setOnKeyListener((v,keyCode,keyEvent) -> enteredUsername(keyCode,keyEvent));
        lvPlayers = findViewById(R.id.lobby_list_players);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players);
        lvPlayers.setAdapter(arrayAdapter);


    }

    /*
    private boolean enteredUsername(int keycode, KeyEvent keyevent) {
        if (keyevent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
            String username = etUsername.getText().toString();
            wizardClient.sendMessage(new LobbyMessage(username));
            debug(username);
            etUsername.setEnabled(false);
            return true;
        }
        return false;
    }
    */

    private void testServer() {
        wizardClient.sendMessage(new TextMessage("Some request"));
    }

    private void startGame() {
        wizardClient.sendMessage(new ActionMessage(START));
    }

    private void connectToServer() {
        wizardClient = WizardClient.getInstance();

        wizardClient.registerCallback(basemessage -> {
            String res = null;
            if (basemessage instanceof TextMessage) {
                info("SERVER RESPONSE:"+ basemessage.toString());
                res = ((TextMessage) basemessage).text;
            }
            else if (basemessage instanceof LobbyMessage) {
                Player player = ((LobbyMessage) basemessage).getPlayer();
                info("Received a LobbyMessage "+player.getName());

                runOnUiThread(() -> {
                    players.add(player.getName());
                    arrayAdapter.notifyDataSetChanged();
                    if (players.size() >= WizardConstants.MIN_NUM_PLAYERS && !etUsername.isEnabled()) {
                        btnStartGame.setEnabled(true);
                    }
                });
            }
            else if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == START)) {
                info(basemessage.toString());
                gameData = new GameData();
                Intent intent = new Intent(this, GameActivity.class);
                //intent.putExtra("myPlayer", myPlayer);
                wizardClient.deregisterCallback();
                startActivity(intent);
            }
            else if ((basemessage instanceof PlayerMessage)) {
                myPlayer = new Player(((PlayerMessage) basemessage).getPlayer().getName(),
                        ((PlayerMessage) basemessage).getPlayer().getConnectionID());
            }
            else {
                error("Not a textmessage: "+basemessage.toString());
                res = "Response is not a TextMessage";
            }
            String finalRes = res;
            runOnUiThread(() ->
                    tvServerResponse.setText(finalRes)
            );
        });

        // wait for the connection to be fully established
        // bad solution, has to be replaced with something like server ping
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String username = etUsername.getText().toString();
        wizardClient.sendMessage(new LobbyMessage(username));
        debug(username);
        etUsername.setEnabled(false);
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
