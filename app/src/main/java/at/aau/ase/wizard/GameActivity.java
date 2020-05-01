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

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;


public class GameActivity extends AppCompatActivity {
    private Button btnShuffle;
    private Button btnDeal;
    private String etShowCard;
    private String textTrumpCard;

    private ImageView ivShowCardJpg;
    private ViewPager2 viewPager2;
    private TextView tv_showTextTrumpf;

    List<SliderItem> sliderItems; //Zeigt scrollHand

    //Test PlayerHand
    Hand playerHand = new Hand();

    //Test table
    Hand table = new Hand();

    //Test Trump hand
    Hand trumpHand = new Hand();

    //test Deck
    Deck deck = new Deck();

    //deafault deal for 10 Playercards and 1 Trumpcard
    public void create10testPlayerCards(Deck deck) {
        playerHand.clear();
        for (int i = 1; i < 11; i++) { //skip first card that is going to be trumpcard
            playerHand.add(deck.getCards().get(i));
        }
        addCardsToSlideView(playerHand.getCards());
        dealTrumpCard();
    }

    public void dealTrumpCard(){
        trumpHand.clear();
        deck.dealCard(deck.getCards().get(0), trumpHand);

        int id = getResources().getIdentifier(trumpHand.getCards().get(0).getPictureFileId(), "drawable", getPackageName());
        if (id == 0) {//if the pictureID is false show Error Logo
            ivShowCardJpg.setImageResource((R.drawable.z0error));

        } else {//show Card
            ivShowCardJpg.setImageResource(id);
            tv_showTextTrumpf.setText(trumpHand.getCards().get(0).toString());
        }
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

    private WizardClient wizardClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);


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

        wizardClient = WizardClient.getInstance();

        //Animation for display Trumpcard as Text
        ivShowCardJpg.setOnClickListener(v -> {
            Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            ivShowCardJpg.startAnimation(aniRotateClk);
            if (tv_showTextTrumpf.getVisibility() == View.VISIBLE) {
                tv_showTextTrumpf.setVisibility(View.INVISIBLE);
            } else {
                tv_showTextTrumpf.setVisibility(View.VISIBLE);
            }
        });
    }

    private void shuffleCards () {
        wizardClient.sendMessage(new ActionMessage(Action.SHUFFLE));
    }

    private void dealCards() {
        deck.shuffle();
        create10testPlayerCards(deck);
    }

}

