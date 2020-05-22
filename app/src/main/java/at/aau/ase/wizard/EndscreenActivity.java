package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EndscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);
        // todo: reset the online players + set Game.runninggame boolean to false.
    }
}
