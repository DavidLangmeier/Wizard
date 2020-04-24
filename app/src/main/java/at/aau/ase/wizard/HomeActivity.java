package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button btnPlay;
    private Button btnSettings;
    private Button btnHowToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnPlay = (Button) findViewById(R.id.home_btn_play);
        btnPlay.setOnClickListener(v -> openLobbyActivity());
        btnSettings = (Button) findViewById(R.id.home_btn_settings);
        btnSettings.setOnClickListener(v -> openSettingsActivity());
        btnHowToPlay = (Button) findViewById(R.id.home_btn_howToPlay);
        btnHowToPlay.setOnClickListener(v -> openSettingsActivity());
    }

    public void openLobbyActivity() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }

    public void openSettingsActivity() {
        // TODO
    }

    public void openHowToPlayActivity() {
        // ToDo
    }
    
}
