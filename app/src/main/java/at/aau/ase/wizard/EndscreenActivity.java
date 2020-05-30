package at.aau.ase.wizard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;

public class EndscreenActivity extends AppCompatActivity {

    private Button btnPlayAgain;
    private Button btnExitGame;
    private TextView tvMyPlayerScore;
    private ListView lvRanking;

    private WizardClient wizardClient;
    GameData gameData;
    List<String> playersInRankingOrder;
    private ArrayAdapter<String> arrayAdapter = null;
    int[][] totalPointsInRankingOrder;


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

        // btnPlayAgain.setOnClickListener();

        String s2 = getIntent().getStringExtra("gameData");
        gameData = new Gson().fromJson(s2, GameData.class);
        sortPlayersByRanking();
        sortPlayerTotalPointsByRanking();
        //System.out.println("--------------------------------------------" + gameData.getScores().getBetTricksPerPlayerPerRound()[0][19]);
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
        arrayAdapter = new LobbyListAdapter(this, playersInRankingOrder, icons);
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
                .boxed().sorted((i, j) -> a[i][0] - a[j][0])
                .mapToInt(e -> e).toArray();
        invert(sortedIndices);
        return sortedIndices;
        /*int[][] a = gameData.getScores().getTotalPointsPerPlayer();
        System.out.println("======================================== " + a.length);
        int[] copySort = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            copySort = a[i];
        }
        int[] index = new int[a.length];
        int counter = 0;
        for (int playerIndex = 0; playerIndex < gameData.getScores().getPlayerNamesList().size(); playerIndex++) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a.length; j++) {
                    if (copySort[i] == a[playerIndex][0]) {
                        index[counter] = j;
                        a[playerIndex][j] = -2101; //max negative points -1
                        counter++;
                        break;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(index));
        return index;*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sortPlayersByRanking(){
        int[] index = rankingIndices();
        List<String> players = gameData.getScores().getPlayerNamesList();
        List<String> returnSortedPlayers = gameData.getScores().getPlayerNamesList();

        for (int i = 0; i < players.size(); i++) {
            returnSortedPlayers.set(i, players.get(index[i]));
        }
        playersInRankingOrder = returnSortedPlayers;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void sortPlayerTotalPointsByRanking(){
        int[] index = rankingIndices();
        int[][] pPP = gameData.getScores().getTotalPointsPerPlayer();
        int[][] returnPPP = gameData.getScores().getTotalPointsPerPlayer();
        for (int i = 0; i < playersInRankingOrder.size(); i++) {
            returnPPP[i][0]=pPP[index[i]][0];
        }
        totalPointsInRankingOrder = returnPPP;
    }

    void invert(int[]array) {
        for (int i = 0; i < array.length/2; i++) {
            int temp = array[i];
            array[i]= array[array.length - 1 - i];
            array[array.length - 1 - i]= temp;
        }
    }
}
