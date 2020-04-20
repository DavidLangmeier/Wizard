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
    private Button btnShuffle, btnDeal;
    String et_showCard;
    TextView tv_valuecolorcard;
    ImageView iv_showCardjpg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnShuffle = (Button) findViewById(R.id.game_btn_shuffleCards);
        btnShuffle.setOnClickListener(v -> shuffleCards());
        btnDeal = (Button) findViewById(R.id.game_btn_dealOutCards);
        btnDeal.setOnClickListener(v -> dealCards());

        //Text View witch show the value und color of the card
        tv_valuecolorcard = findViewById(R.id.etn_showCard);
        //ImageView
        iv_showCardjpg = (ImageView) findViewById(R.id.im_firstCard);

        //hardcoded Karten zum Testen ++++++++++++++++++++++++++ server Antwort Eintragen++++++++++++++++++++++++++++++++++++++++
        Card card1 = new Card(Color.GREEN, Value.ELEVEN);

        int id = getResources().getIdentifier(card1.getPictureFileId(), "drawable", getPackageName());
        String str_id = Integer.toString(id);
      // tv_valuecolorcard.setText(str_id);
        if(id==0){//if the pictureID is false show Error Logo
            iv_showCardjpg.setImageResource((R.drawable.z0error));
        }else {//show Card
            iv_showCardjpg.setImageResource(id);
        }
    }

    private void shuffleCards () {
        // TODO
    }

    private void dealCards () {
        // TODO
    }

}
