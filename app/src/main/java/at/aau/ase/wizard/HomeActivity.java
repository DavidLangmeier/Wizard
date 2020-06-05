package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button btnPlay;
    private Button btnHowToPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnPlay = (Button) findViewById(R.id.home_btn_play);
        btnPlay.setOnClickListener(v -> openLobbyActivity());
        btnHowToPlay = (Button) findViewById(R.id.home_btn_howToPlay);
        btnHowToPlay.setOnClickListener(v -> openHowToPlayActivity());
    }

    public void openLobbyActivity() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }

    public void openHowToPlayActivity() {
        Intent intent2 = new Intent(this, PlayPdfActivity.class);
        startActivity(intent2);
    }
    
}
