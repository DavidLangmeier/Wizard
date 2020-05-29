package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;

public class EndscreenActivity extends AppCompatActivity {

    private Button btnPlayAgain;
    private Button btnExitGame;
    private TextView tvMyPlayerScore;

    private WizardClient wizardClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        wizardClient = WizardClient.getInstance();
        startCallback();

       // btnPlayAgain.setOnClickListener();

        String s2 = getIntent().getStringExtra("gameData");
        GameData gameData = new Gson().fromJson(s2, GameData.class);
        System.out.println("--------------------------------------------" + gameData.getScores().getBetTricksPerPlayerPerRound()[0][19]);
        //tvMyPlayerScore.findViewById(R.id.tv_myPlayerScores);
        //tvMyPlayerScore.setText(gameData.getScores().getBetTricksPerPlayerPerRound()[0][19]);
    }

    public void startCallback() {
        wizardClient.registerCallback(basemessage -> {
            if(basemessage instanceof ActionMessage && ((ActionMessage) basemessage).getActionType() == Action.END){
                // todo: reset the online players + set Game.runninggame boolean to false.
            }
        });
    }
}
