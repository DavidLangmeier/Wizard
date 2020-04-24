package at.aau.ase.wizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;


public class GameActivity extends AppCompatActivity {
    private Button btnShuffle;
    private Button btnDeal;
    private String etShowCard;
    private TextView tvValueColorCard;
    private ImageView ivShowCardJpg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnShuffle = (Button) findViewById(R.id.game_btn_shuffleCards);
        btnShuffle.setOnClickListener(v -> shuffleCards());
        btnDeal = (Button) findViewById(R.id.game_btn_dealOutCards);
        btnDeal.setOnClickListener(v -> dealCards());

        //Text View witch show the value und color of the card
        tvValueColorCard = findViewById(R.id.etn_showCard);
        //ImageView
        ivShowCardJpg = (ImageView) findViewById(R.id.im_firstCard);

        //hardcoded Karten zum Testen ++++++++++++++++++++++++++ server Antwort Eintragen++++++++++++++++++++++++++++++++++++++++
        Card card1 = new Card(Color.GREEN, Value.ELEVEN);

        int id = getResources().getIdentifier(card1.getPictureFileId(), "drawable", getPackageName());
        if(id==0){//if the pictureID is false show Error Logo
            ivShowCardJpg.setImageResource((R.drawable.z0error));
        }else {//show Card
            ivShowCardJpg.setImageResource(id);
        }
    }

    private void shuffleCards () {
        // TODO
    }

    private void dealCards () {
        // TODO
    }

}
