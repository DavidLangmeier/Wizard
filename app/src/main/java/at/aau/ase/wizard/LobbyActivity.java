package at.aau.ase.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LobbyActivity extends AppCompatActivity {
    Button btnServer, btnClient, btnToGameScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnServer = findViewById(R.id.lobby_btn_startServer);
        btnServer.setOnClickListener(v -> startServer());
        btnClient = findViewById(R.id.lobby_btn_startClient);
        btnClient.setOnClickListener(v -> startClient());
        btnToGameScreen = (findViewById(R.id.lobby_btn_ToGameScreen));
        btnToGameScreen.setOnClickListener(v -> openGameActivity());
    }

    private void startServer() {
        // todo
    }

    private void startClient() {
        // todo
    }

    private void openGameActivity() {
        startActivity(new Intent(this, GameActivity.class));
    }
}
