package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;

public class EndscreenActivity extends AppCompatActivity {

    private Button btnPlayAgain;
    private Button btnExitGame;
    private TextView tvMyPlayerScore;

    private WizardClient wizardClient;
    GameData gameData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        wizardClient = WizardClient.getInstance();
        startCallback();

        // btnPlayAgain.setOnClickListener();

        String s2 = getIntent().getStringExtra("gameData");
        gameData = new Gson().fromJson(s2, GameData.class);
        //System.out.println("--------------------------------------------" + gameData.getScores().getBetTricksPerPlayerPerRound()[0][19]);
        tvMyPlayerScore = findViewById(R.id.tv_myPlayerScores);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnExitGame = findViewById(R.id.btn_exit_game);
        String winnerText = gameData.getScores().getPlayerNamesList().get(checkWinner()) + " wins with " + Arrays.toString(gameData.getScores().getTotalPointsPerPlayer()[checkWinner()]) + " points!";
        tvMyPlayerScore.setText(winnerText);
    }

    public void startCallback() {
        wizardClient.registerCallback(basemessage -> {
            if (basemessage instanceof ActionMessage && ((ActionMessage) basemessage).getActionType() == Action.END) {
                // todo: reset the online players + set Game.runninggame boolean to false.
            }
        });
    }

    int checkWinner() {
        int winnerIndex = 0;
        for (int i = 1; i < gameData.getScores().getPlayerNamesList().size(); i++) {
            if (gameData.getScores().getTotalPointsPerPlayer()[i - 1][0] < gameData.getScores().getTotalPointsPerPlayer()[i][0]) {
                winnerIndex = i;
            }
        }
        return winnerIndex;
    }
}
