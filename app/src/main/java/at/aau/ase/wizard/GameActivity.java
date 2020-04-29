package at.aau.ase.wizard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Color;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Value;


public class GameActivity extends AppCompatActivity {
    private Button btnShuffle;
    private Button btnDeal;
    private String etShowCard;

    private ImageView ivShowCardJpg;
    private ViewPager2 viewPager2;
    private TextView tv_showTextTrumpf;

    List<SliderItem> sliderItems; //Zeigt scrollHand

    //Test karten
    ArrayList<Card> playerCards;

    //Test PlayerHand
    Hand playerHand = new Hand();

    //test Deck
    Deck deck = new Deck();


    public void create10testPlayerCards(Deck deck) {
        playerHand.clear();
        for (int i = 0; i < 10; i++) {
            playerHand.add(deck.getCards().get(i));
        }
        addCardsToSlideView(playerHand.getCards());

        /*
        playerCards.add(new Card(Color.BLUE, Value.FIVE));
        playerCards.add(new Card(Color.RED, Value.FIVE));
        playerCards.add(new Card(Color.GREEN, Value.FIVE));
        playerCards.add(new Card(Color.YELLOW, Value.FIVE));

        playerCards.add(new Card(Color.BLUE, Value.NINE));
        playerCards.add(new Card(Color.RED, Value.EIGHT));
        playerCards.add(new Card(Color.GREEN, Value.FIVE));
        playerCards.add(new Card(Color.YELLOW, Value.ONE));
        */
    }

    //------------Metode SPIEILKARTEN  vom Server Anzeigen in spielhand//--------------------------
    public void addCardsToSlideView(ArrayList<Card> pp_playerCards) {
        playerHand.setCards(pp_playerCards);

        sliderItems.clear(); //Clear wennn neue Carten von Server geschickt werden


        for (int i = 0; i < pp_playerCards.size(); i++) {
            //umwandlung Color.Red, Value.Five in drawable picture alternativ (sliderItems.add(new SliderItem(R.drawable.red0eight));
            int id = getResources().getIdentifier(pp_playerCards.get(i).getPictureFileId(), "drawable", getPackageName());

            if (id == 0) {//if the pictureID is false show Error Logo zero
                sliderItems.add(new SliderItem((R.drawable.z0error)));
            } else {//show Card
                sliderItems.add(new SliderItem(id));
            }
        }
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        playerCards = new ArrayList<>();

        btnShuffle = (Button) findViewById(R.id.game_btn_shuffleCards);
        btnShuffle.setOnClickListener(v -> shuffleCards());
        btnDeal = (Button) findViewById(R.id.game_btn_dealOutCards);
        btnDeal.setOnClickListener(v -> dealCards());

        tv_showTextTrumpf = (TextView) findViewById(R.id.tv_trumpftext);

        viewPager2 = findViewById(R.id.viewPagerImageSlieder);

        //List of Images from drawable
        sliderItems = new ArrayList<>();


        //Damit mehrere nebeneinander sichbar sind
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(10);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(0));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        //ende der sichtbarkeit von mehreren hintereinander ----------------------------------------
        //ImageView
        ivShowCardJpg = (ImageView) findViewById(R.id.im_firstCard);

        //------------Metode TRUMP  vom Server Anzeigen in am Rand//--------------------------------
        //
        Card card1 = new Card(Color.GREEN, Value.ELEVEN);

        int id = getResources().getIdentifier(card1.getPictureFileId(), "drawable", getPackageName());
        if (id == 0) {//if the pictureID is false show Error Logo
            ivShowCardJpg.setImageResource((R.drawable.z0error));
        } else {//show Card
            ivShowCardJpg.setImageResource(id);
        }
        ivShowCardJpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                ivShowCardJpg.startAnimation(aniRotateClk);
                if (tv_showTextTrumpf.getVisibility() == View.VISIBLE) {
                    tv_showTextTrumpf.setVisibility(View.INVISIBLE);
                } else {
                    tv_showTextTrumpf.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void shuffleCards() {
        deck.shuffle();
        create10testPlayerCards(deck);
    }

    private void dealCards() {
        // TODO
    }

}

