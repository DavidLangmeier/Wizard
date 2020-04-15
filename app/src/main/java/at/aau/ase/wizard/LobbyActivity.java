package at.aau.ase.wizard;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LobbyActivity extends AppCompatActivity {
    Button btnServer, btnClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnServer = findViewById(R.id.lobby_btn_startServer);
        btnServer.setOnClickListener(v -> startServer());
        btnClient = findViewById(R.id.lobby_btn_startClient);
        btnClient.setOnClickListener(v -> startClient());
    }

    private void startServer() {
        // todo
    }

    private void startClient() {
        // todo
    }
}
