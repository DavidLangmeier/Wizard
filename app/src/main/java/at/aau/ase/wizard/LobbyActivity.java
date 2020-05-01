package at.aau.ase.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;

import static com.esotericsoftware.minlog.Log.*;

public class LobbyActivity extends AppCompatActivity {
    private Button btnServer;
    private Button btnToGameScreen;
    private WizardClient wizardClient = null;
    private TextView tvServerResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnServer = findViewById(R.id.lobby_btn_testServer);
        btnServer.setOnClickListener(v -> startServer());
        btnToGameScreen = (findViewById(R.id.lobby_btn_ToGameScreen));
        btnToGameScreen.setOnClickListener(v -> openGameActivity());

        tvServerResponse = findViewById(R.id.lobby_text_serverResponseDisplay);
        wizardClient = WizardClient.getInstance();
        wizardClient.registerCallback(basemessage -> {
            String res = null;
            if (basemessage instanceof TextMessage) {
                info("SERVER RESPONSE:"+ basemessage.toString());
                res = ((TextMessage) basemessage).text;
            } else {
                error("Not a textmessage: "+basemessage.toString());
                res = "Response is not a TextMessage";
            }
            String finalRes = res;
            runOnUiThread(() ->
                    tvServerResponse.setText(finalRes)
            );
        });
    }
  
    private void startServer() {
        wizardClient.sendMessage(new TextMessage("Some request"));
    }

    private void openGameActivity() {
        startActivity(new Intent(this, GameActivity.class));
    }
}
