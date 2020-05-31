package at.aau.ase.wizard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import static com.esotericsoftware.minlog.Log.info;

public class EndscreenActivity extends AppCompatActivity {

    private Button btnPlayAgain;
    private Button btnExitGame;
    private TextView tvMyPlayerScore;
    private ListView lvRanking;

    private WizardClient wizardClient;
    GameData gameData;
    List<String> playersInRankingOrder;
    private EndscreenListAdapter arrayAdapter = null;
    int[][] totalPointsInRankingOrder;
    int[] actualIconID;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        tvMyPlayerScore = findViewById(R.id.tv_myPlayerScores);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnExitGame = findViewById(R.id.btn_exit_game);
        lvRanking = findViewById(R.id.lv_PlayerScores);

        wizardClient = WizardClient.getInstance();
        startCallback();

        String s2 = getIntent().getStringExtra("gameData");
        gameData = new Gson().fromJson(s2, GameData.class);
        sortPlayersByRanking();
        sortPlayerTotalPointsByRanking();
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
            if (basemessage instanceof ActionMessage && ((ActionMessage) basemessage).getActionType() == Action.END) {
                // todo: reset the online players + set Game.runninggame boolean to false.
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    int[] rankingIndices() {
        int[][] a = gameData.getScores().getTotalPointsPerPlayer();
        int[] sortedIndices = IntStream.range(0, a.length)
                .boxed().sorted(Comparator.comparingInt(i -> a[i][0]))
                .mapToInt(e -> e).toArray();
        invert(sortedIndices);
        return sortedIndices;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sortPlayersByRanking() {
        int[] index = rankingIndices();
        info(Arrays.toString(index));
        List<String> players = gameData.getScores().getPlayerNamesList();
        String playerNames = "PLAYERNAMES: " + players.toString();
        info(playerNames);
        List<String> returnSortedPlayers = gameData.getScores().getPlayerNamesList();
        playerNames = "PLAYERNAMES: " + returnSortedPlayers.toString();
        info(playerNames);
        playersInRankingOrder = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            playersInRankingOrder.add(i, players.get(index[i]));
        }
        info(playersInRankingOrder.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sortPlayerTotalPointsByRanking() {
        int[] index = rankingIndices();
        int[][] pPP = gameData.getScores().getTotalPointsPerPlayer();
        int[][] returnPPP = gameData.getScores().getTotalPointsPerPlayer();
        for (int i = 0; i < playersInRankingOrder.size(); i++) {
            returnPPP[i][0] = pPP[index[i]][0];
        }
        totalPointsInRankingOrder = returnPPP;
        setActualIconID();
    }

    void setActualIconID() {
        actualIconID = new int[totalPointsInRankingOrder.length];
        int counter = 0;
        for (int i = 1; i < totalPointsInRankingOrder.length; i++) {
            if (totalPointsInRankingOrder[i - 1][0] == totalPointsInRankingOrder[i][0]) {
                actualIconID[i] = counter;
            } else {
                actualIconID[i] = ++counter;
            }
        }

    }

    void invert(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}
