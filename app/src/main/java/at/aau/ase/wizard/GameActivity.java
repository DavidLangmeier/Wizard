package at.aau.ase.wizard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
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
    Dialog dialog;


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
        dialog = new Dialog(this);
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

    public void ShowPopupBlockofTruth(View v) {
        TextView txtclose;
        TextView np_Rounds;
        TextView np_pointsPlayer1;
        TextView np_vorherSagePlayer1;
        TextView np_pointsPlayer2;
        TextView np_vorherSagePlayer2;
        TextView np_vorherSagePlayer;
        TextView np_pointsPlayer;
        TextView np_PlayerNames;

        dialog.setContentView(R.layout.activity_game_popup);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        // txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //-------------------------------Runden Befüllen   1-20---------------------------
        np_Rounds = (TextView) dialog.findViewById(R.id.tv_rounds);
        String round = "";

        short[] RoundsArray = new short[21];
        for (int i = 0; i < RoundsArray.length; i++) {
            RoundsArray[i] = (short) i;
        }
        for (int i = 1; i < RoundsArray.length; i++) {
            round = round + String.valueOf(RoundsArray[i]);
            round = round + System.lineSeparator();
        }
        np_Rounds.setText(round);


        //-----------------------Player Befüllung Namen ---------------------------
        //ersetzung durch daten erhalten von server
        Notepad testNodepade = new Notepad((short) 3);
        testNodepade.testFillPointsPlayerround();

        np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player1);
        for (int i = 0; i < testNodepade.playerNames.length; i++) {

            switch (i) {
                case 0:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player1);
                    break;
                case 1:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player2);
                    break;
                case 2:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player3);
                    break;
                case 3:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player4);
                    break;
                case 4:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player5);
                    break;
                case 5:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player6);
                    break;

                default:
                    np_PlayerNames = (TextView) dialog.findViewById(R.id.tv_player6);
            }
            np_PlayerNames.setText(testNodepade.playerNames[i]);
        }

        //------------------Punkte Ausgabe Player  3 4 5 6-----------------------------------

        for (int i = 0; i < testNodepade.pointsPerPlayerPerRound.length; i++) {
            String testPlayerpoints1 = "";
            switch (i) {
                case 0:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points1);
                    break;
                case 1:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points2);
                    break;
                case 2:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points3);
                    break;
                case 3:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points4);
                    break;
                case 4:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points5);
                    break;
                case 5:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points6);
                    break;

                default:
                    np_pointsPlayer = (TextView) dialog.findViewById(R.id.tv_points6);
            }
            for (int j = 0; j < testNodepade.pointsPerPlayerPerRound[i].length; j++) {
                testPlayerpoints1 = testPlayerpoints1 + String.valueOf(testNodepade.pointsPerPlayerPerRound[i][j]);
                testPlayerpoints1 = testPlayerpoints1 + System.lineSeparator();
            }
            np_pointsPlayer.setText(testPlayerpoints1);
        }
            //------------------Vorhersage Ausgabe Player 1 2 3 4 5 6--------------------------

        for (int i = 0; i < testNodepade.pointsPerPlayerPerRound.length; i++) {
            String testVorhersage = "";
            switch (i) {
                case 0:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks1);
                    break;
                case 1:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks2);
                    break;
                case 2:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks3);
                    break;
                case 3:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks4);
                    break;
                case 4:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks5);
                    break;
                case 5:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks6);
                    break;

                default:
                    np_vorherSagePlayer = (TextView) dialog.findViewById(R.id.tv_pointstricks6);
            }
            for (int j = 0; j < testNodepade.pointsPerPlayerPerRound[i].length; j++) {
                testVorhersage = testVorhersage + String.valueOf(testNodepade.pointsPerPlayerPerRound[i][j]);
                testVorhersage = testVorhersage + System.lineSeparator();
            }
            np_vorherSagePlayer.setText(testVorhersage);
        }

        /*
        //-----------------------------Vorhersagen Ausgabe Player 1 ----------------------------
        np_vorherSagePlayer1=(TextView) dialog.findViewById(R.id.tv_pointstricks1);
        Notepad testPoints=new Notepad((short)3);
        testPoints.testFillPointsPlayerround();
        String vorhersage="";
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < testPoints.pointsPerPlayerPerRound[i].length; j++) {
                vorhersage = vorhersage + String.valueOf(testPoints.pointsPerPlayerPerRound[i][j]);
                vorhersage = vorhersage + System.lineSeparator();
            }
        }
        np_vorherSagePlayer1.setText(vorhersage);
        //-------------------------------Punkte Ausgabe Player 2---------------------------------
        np_pointsPlayer2=(TextView) dialog.findViewById(R.id.tv_points2);
        String testPlayerPoints2="";
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < testNodepade.pointsPerPlayerPerRound[i].length; j++) {
                testPlayerPoints2 = testPlayerPoints2 + String.valueOf(testNodepade.pointsPerPlayerPerRound[i][j]);
                testPlayerPoints2 = testPlayerPoints2 + System.lineSeparator();
            }
        }
        np_pointsPlayer2.setText(testPlayerPoints2);


        //-------------------------------Vorhersage Ausgabe Player 2------------------------
        String teste="2";
        np_vorherSagePlayer2=(TextView) dialog.findViewById(R.id.tv_pointstricks2);
        String vorhersage2="";
        for (int i = 1; i < 2; i++) {
            for (int j = 0; j < testPoints.pointsPerPlayerPerRound[i].length; j++) {
                vorhersage2 = vorhersage2 + String.valueOf(testPoints.pointsPerPlayerPerRound[i][j]);
                vorhersage2 = vorhersage2 + System.lineSeparator();
            }
        }
        np_vorherSagePlayer2.setText(vorhersage2);
        */
        dialog.show();

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

                if (gameData.getTable().getCards().size() != 0) {
                    ArrayList<Card> cardsOnTable = gameData.getTable().getCards();
                    runOnUiThread(() -> showTableCards(cardsOnTable));
                }

                if (gameData.getActivePlayer() == (myPlayer.getConnectionID() - 1)) {
                    runOnUiThread(() ->
                            btnPlaySelectedCard.setEnabled(true));
                } else {
                    runOnUiThread(() ->
                            btnPlaySelectedCard.setEnabled(false));
                }

            } else if (basemessage instanceof HandMessage) {
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

    private void showTableCards(ArrayList<Card> cards) {
        int cardID;
        switch (cards.size()) {
            case 6:
                cardID = getResources().getIdentifier(cards.get(5).getPictureFileId(), "drawable", getPackageName());
                ivTable6.setImageResource(cardID);
            case 5:
                cardID = getResources().getIdentifier(cards.get(4).getPictureFileId(), "drawable", getPackageName());
                ivTable5.setImageResource(cardID);
            case 4:
                cardID = getResources().getIdentifier(cards.get(3).getPictureFileId(), "drawable", getPackageName());
                ivTable4.setImageResource(cardID);
            case 3:
                cardID = getResources().getIdentifier(cards.get(2).getPictureFileId(), "drawable", getPackageName());
                ivTable3.setImageResource(cardID);
            case 2:
                cardID = getResources().getIdentifier(cards.get(1).getPictureFileId(), "drawable", getPackageName());
                ivTable2.setImageResource(cardID);
            case 1:
                cardID = getResources().getIdentifier(cards.get(0).getPictureFileId(), "drawable", getPackageName());
                ivTable1.setImageResource(cardID);
                break;
            default:
                System.out.println("Table Hand too short or too big! Something strange happened...");
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