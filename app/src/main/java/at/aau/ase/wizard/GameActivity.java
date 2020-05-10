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

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;

import java.util.ArrayList;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.DEAL;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.READY;
import static com.esotericsoftware.minlog.Log.info;


public class GameActivity extends AppCompatActivity {
    private Button btnPlaySelectedCard;
    private Button btnDeal;
    private String etShowCard;
    private String textTrumpCard;
    private ImageView ivShowTrumpCard;
    private ImageView ivTable1, ivTable2, ivTable3, ivTable4, ivTable5, ivTable6;
    private ViewPager2 viewPager2;
    private TextView tv_showTextTrumpf;
    private static WizardClient wizardClient = LobbyActivity.getWizardClient();
    private List<SliderItem> sliderItems = new ArrayList<>(); //Zeigt scrollHand
    private Player myPlayer = LobbyActivity.getMyPlayer();
    private static GameData gameData = LobbyActivity.getGameData();
    private SliderAdapter sliderAdapter; //to access player Card from Scrollhand later


    Hand myHand = new Hand(); //Test PlayerHand
    Hand table = new Hand(); //Test Table
    Hand trumpHand = new Hand(); //Test TrumpHand
    Deck deck = new Deck(); //Test Deck

    // onCreate() is overused, has to be cleaned up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //wizardClient = WizardClient.getInstance(); // new instance would get new connectionID, has to be fixed
        startCallback();

        //myPlayer = LobbyActivity.getMyPlayer(); // use intent extra if problems with activity lifecycle occur
        //myPlayer = (Player) getIntent().getSerializableExtra("myPlayer"); // does not work properly - use bundle?
        info("@GAME_ACTIVITY: My Playername=" + myPlayer.getName() + ", connectionID=" + myPlayer.getConnectionID());

        btnPlaySelectedCard = findViewById(R.id.play_Card);
        btnPlaySelectedCard.setOnClickListener(v -> dealOnePlayerCardOnTable());
        btnPlaySelectedCard.setEnabled(false); // Button has to be removed later
        btnDeal = findViewById(R.id.game_btn_dealOutCards);
        btnDeal.setOnClickListener(v -> dealCards());
        btnDeal.setEnabled(true);
        tv_showTextTrumpf = (TextView) findViewById(R.id.tv_trumpftext);

        ivTable1 = findViewById(R.id.tableCard1);
        ivTable2 = findViewById(R.id.tableCard2);
        ivTable3 = findViewById(R.id.tableCard3);
        ivTable4 = findViewById(R.id.tableCard4);
        ivTable5 = findViewById(R.id.tableCard5);
        ivTable6 = findViewById(R.id.tableCard6);

        viewPager2 = findViewById(R.id.viewPagerImageSlieder);
        //sliderItems = new ArrayList<>();    //List of Images from drawable

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
        ivShowTrumpCard = (ImageView) findViewById(R.id.viewTrumpCard);

        //Animation for display Trumpcard as Text
        ivShowTrumpCard.setOnClickListener(v -> showTrump());
    }

    public void startCallback() {
        wizardClient.registerCallback(basemessage -> {
            if (basemessage instanceof StateMessage) {
                info("GAME_ACTIVITY: StateMessage received.");
                gameData.updateState((StateMessage) basemessage);
                /*if (gameData.getDealer() == (myPlayer.getConnectionID()-1)) {
                    runOnUiThread(() ->
                            btnDeal.setEnabled(true));
                } else {
                    runOnUiThread(() ->
                            btnDeal.setEnabled(false));
                }*/
                //TODO should show cards on Table but app crashes when executed here
                //showTableCards();


                if (gameData.getActivePlayer() == (myPlayer.getConnectionID()-1)) {
                    runOnUiThread(() ->
                            btnPlaySelectedCard.setEnabled(true));
                } else {
                    runOnUiThread(() ->
                    btnPlaySelectedCard.setEnabled(false));
                }

            }
            else if (basemessage instanceof HandMessage) {
                info("GAME_ACTIVITY: Hand recieved.");
                gameData.setMyHand((HandMessage) basemessage);
                runOnUiThread(() ->
                        addCardsToSlideView(gameData.getMyHand().getCards()));


            }

        });
        wizardClient.sendMessage(new ActionMessage(READY));
        info("GAME_ACTIVITY: READY sent.");
    }

    public void showTrump() {
        Animation aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        ivShowTrumpCard.startAnimation(aniRotateClk);
        if (tv_showTextTrumpf.getVisibility() == View.VISIBLE) {
            tv_showTextTrumpf.setVisibility(View.INVISIBLE);
        } else {
            tv_showTextTrumpf.setVisibility(View.VISIBLE);
        }
    }

    //default deal for 10 Playercards and 1 Trumpcard
    public void create10testPlayerCards(Deck deck) {
        myHand.clear();
        for (int i = 1; i < 11; i++) { //skip first card that is going to be trumpcard
            myHand.add(deck.getCards().get(i));
        }
        addCardsToSlideView(myHand.getCards());
        dealTrumpCard();
    }

    public void dealTrumpCard() {
        trumpHand.clear();
        deck.dealCard(deck.getCards().get(0), trumpHand);

        int id = getResources().getIdentifier(trumpHand.getCards().get(0).getPictureFileId(), "drawable", getPackageName());
        if (id == 0) {//if the pictureID is false show Error Logo
            ivShowTrumpCard.setImageResource((R.drawable.z0error));

        } else {//show Card
            ivShowTrumpCard.setImageResource(id);
            tv_showTextTrumpf.setText(trumpHand.getCards().get(0).toString());
        }
    }

    //------------Methode SPIELKARTEN vom Server Anzeigen in spielhand//--------------------------
    public void addCardsToSlideView(ArrayList<Card> pp_playerCards) {
        //myHand.setCards(pp_playerCards);
        sliderItems.clear(); //Clear wennn neue Carten von Server geschickt werden

        for (int i = 0; i < pp_playerCards.size(); i++) {
            //umwandlung Color.Red, Value.Five in drawable picture alternativ (sliderItems.add(new SliderItem(R.drawable.red0eight));
            int id = getResources().getIdentifier(pp_playerCards.get(i).getPictureFileId(), "drawable", getPackageName());

            if (id == 0) {//if the pictureID is false show Error Logo zero
                sliderItems.add(new SliderItem((R.drawable.z0error), pp_playerCards.get(i)));
            } else {//show Card
                sliderItems.add(new SliderItem(id, pp_playerCards.get(i)));
            }
        }
        viewPager2.setAdapter(sliderAdapter = new SliderAdapter(sliderItems, viewPager2));
    }

    private void showTableCards() {
        int card_id;
        switch (gameData.getTable().getCards().size()) {
            case 6:
                card_id = getResources().getIdentifier(gameData.getTable().getCards().get(5).toString(), "drawable", getPackageName());
                ivTable6.setImageResource(card_id);
            case 5:
                card_id = getResources().getIdentifier(gameData.getTable().getCards().get(5).toString(), "drawable", getPackageName());
                ivTable5.setImageResource(card_id);
            case 4:
                card_id = getResources().getIdentifier(gameData.getTable().getCards().get(5).toString(), "drawable", getPackageName());
                ivTable4.setImageResource(card_id);
            case 3:
                card_id = getResources().getIdentifier(gameData.getTable().getCards().get(5).toString(), "drawable", getPackageName());
                ivTable3.setImageResource(card_id);
            case 2:
                card_id = getResources().getIdentifier(gameData.getTable().getCards().get(5).toString(), "drawable", getPackageName());
                ivTable2.setImageResource(card_id);
            case 1:
                card_id = getResources().getIdentifier(gameData.getTable().getCards().get(1).toString(), "drawable", getPackageName());
                ivTable1.setImageResource(card_id);
                break;

            default:
                System.out.println("Table is empty or something went wrong.");
        }


    }

    private void shuffleCards() {
        wizardClient.sendMessage(new ActionMessage(DEAL));
    }

    private void dealCards() {
        wizardClient.sendMessage(new ActionMessage(DEAL));
    }

    private void dealOnePlayerCardOnTable() {
        wizardClient.sendMessage(new CardMessage(sliderAdapter.getSelectedCard()));
    }

}