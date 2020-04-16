package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {
    private Button btnShuffle, btnDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnShuffle = (Button) findViewById(R.id.game_btn_shuffleCards);
        btnShuffle.setOnClickListener(v -> shuffleCards());
        btnDeal = (Button) findViewById(R.id.game_btn_dealOutCards);
        btnDeal.setOnClickListener(v -> dealCards());
    }

    private void shuffleCards () {
        // TODO
    }

    private void dealCards () {
        // TODO
    }
}
