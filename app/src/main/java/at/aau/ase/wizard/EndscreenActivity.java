package at.aau.ase.wizard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.EXIT;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.START;
import static com.esotericsoftware.minlog.Log.info;

public class EndscreenActivity extends AppCompatActivity {

    private Button btnPlayAgain;
    private Button btnExitGame;
    private TextView tvMyPlayerScore;
    private ListView lvRanking;
    private Player myPlayer;

    private WizardClient wizardClient;
    GameData gameData;
    List playersInRankingOrder = new ArrayList();
    List playersOnline;
    private EndscreenListAdapter arrayAdapter = null;
    int[][] totalPointsInRankingOrder;
    int[] actualIconID;
    Notepad endscreenScores;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        tvMyPlayerScore = findViewById(R.id.tv_myPlayerScores);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnPlayAgain.setOnClickListener(v -> playAgain());
        btnExitGame = findViewById(R.id.btn_exit_game);
        btnExitGame.setOnClickListener(v -> exitGame());
        lvRanking = findViewById(R.id.lv_PlayerScores);

        wizardClient = WizardClient.getInstance();
        startCallback();

        String myPlayer = getIntent().getStringExtra("myPlayer");
        this.myPlayer = new Gson().fromJson(myPlayer, Player.class);
        String playersOnline = getIntent().getStringExtra("playersOnline");
        this.playersOnline = new Gson().fromJson(playersOnline, List.class);
        String scores = getIntent().getStringExtra("endscreenScores");
        this.endscreenScores = new Gson().fromJson(scores, Notepad.class);
        playersInRankingOrder = endscreenScores.getPlayerNamesList();
        totalPointsInRankingOrder = new int[endscreenScores.playerNamesList.size()][1];
        totalPointsInRankingOrder = endscreenScores.getTotalPointsPerPlayer();
        String sortedIconID = getIntent().getStringExtra("sortedIconID");
        actualIconID = new Gson().fromJson(sortedIconID, int[].class);

        String winnerText = playersInRankingOrder.get(0) + " wins with " + totalPointsInRankingOrder[0][0] + " points!";
        tvMyPlayerScore.setText(winnerText);
        Integer[] icons = new Integer[]{
                R.drawable.rank1gold,
                R.drawable.rank2silver,
                R.drawable.rank3bronze,
                R.drawable.rank0default,
                R.drawable.rank0default,
                R.drawable.rank0default
        };
       arrayAdapter = new EndscreenListAdapter(this, playersInRankingOrder, totalPointsInRankingOrder, icons, actualIconID);
       lvRanking.setAdapter(arrayAdapter);
    }

    public void startCallback() {
        wizardClient.registerCallback(basemessage -> {
            if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == START)) {
                info(basemessage.toString());
                gameData = new GameData();
                Intent intent = new Intent(this, GameActivity.class);
                wizardClient.deregisterCallback();
                intent.putExtra("myPlayer", (new Gson()).toJson(myPlayer));
                intent.putExtra("gameData", (new Gson()).toJson(gameData));
                intent.putExtra("playersOnline", (new Gson()).toJson(playersOnline));
                startActivity(intent);
            }
            else if ((basemessage instanceof ActionMessage) && (((ActionMessage) basemessage).getActionType() == EXIT)) {
                info(basemessage.toString());
                Intent intent = new Intent(this, LobbyActivity.class);
                wizardClient.deregisterCallback();
                startActivity(intent);
            }
        });
    }

    void playAgain(){
        wizardClient.sendMessage(new ActionMessage(START));
    }

    void exitGame(){
        wizardClient.sendMessage(new ActionMessage(EXIT));
    }
}
