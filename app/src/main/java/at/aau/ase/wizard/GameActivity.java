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
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.ErrorMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.GoodbyeMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LifecycleMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Deck;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Hand;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.DEAL;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.READY;
import static com.esotericsoftware.minlog.Log.*;


public class GameActivity extends AppCompatActivity {
    private Button btnPlaySelectedCard;
    private Button btnDeal;
    private ImageView ivShowTrumpCard;
    private ImageView ivTable1;
    private ImageView ivTable2;
    private ImageView ivTable3;
    private ImageView ivTable4;
    private ImageView ivTable5;
    private ImageView ivTable6;
    private ViewPager2 viewPager2;
    private TextView tv_showTextTrumpf;
    private WizardClient wizardClient;
    private List<SliderItem> sliderItems = new ArrayList<>(); //Zeigt scrollHand
    private Player myPlayer;
    private GameData gameData;
    private SliderAdapter sliderAdapter; //to access player Card from Scrollhand later
    private Dialog dialog;
    private TextView tvServerMsg;
    private EditText etVorhersage;


    Hand myHand = new Hand(); //Test PlayerHand
    Hand table = new Hand(); //Test Table
    Hand trumpHand = new Hand(); //Test TrumpHand
    Deck deck = new Deck(); //Test Deck

    // onCreate() is overused, has to be cleaned up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String s1 = getIntent().getStringExtra("myPlayer");
        myPlayer = new Gson().fromJson(s1, Player.class);

        String s2 = getIntent().getStringExtra("gameData");
        gameData = new Gson().fromJson(s2, GameData.class);

        wizardClient = WizardClient.getInstance(); // new instance would get new connectionID, has to be fixed
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
        tvServerMsg = findViewById(R.id.game_textView_serverMsg);


        etVorhersage = findViewById(R.id.etn_Vorhersage);
        etVorhersage.setInputType(InputType.TYPE_CLASS_NUMBER);
        etVorhersage.setVisibility(View.INVISIBLE);


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

        etVorhersage.setOnKeyListener((v, keyCode, keyEvent) -> enteredPrediction(keyCode, keyEvent));
    }

    public void showNodepad(View v) {
        //View wird im XML aufgerufen
        TextView txtclose;

        Switch swChangeViewPointsStiche;

        dialog.setContentView(R.layout.activity_game_popup);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ersetzung durch daten erhalten von server----------------------------------------------------------------------
        Notepad testNodepade = new Notepad((short) 3);
        testNodepade.testFillPointsPlayerround();


        //----------------------switch-------------------------

        swChangeViewPointsStiche = (Switch) dialog.findViewById(R.id.sw_switch1);

        swChangeViewPointsStiche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //wenn Switch isCheck fill with Points
                    actualPoints(testNodepade);
                } else {
                    actualTricks(testNodepade);
                }
            }
        });


        //Befüllung der Runden Zahl
        fillRoundsNumber();
        //Player Namen Nodepad Befüllung
        showNamesOfPlayers(testNodepade);
        //Aktuelle Punkte Nodepad Befüllung
        actualPoints(testNodepade);
        //Vorhersage Stiche Nodepad Befüllung
        predictedTricks(testNodepade);

        dialog.show();
    }

    public void fillRoundsNumber() {
        TextView npRounds;
        npRounds = (TextView) dialog.findViewById(R.id.tv_rounds);
        String round = "";

        short[] roundsArray = new short[21];
        for (int i = 0; i < roundsArray.length; i++) {
            roundsArray[i] = (short) i;
        }
        for (int i = 1; i < roundsArray.length; i++) {
            round = round + String.valueOf(roundsArray[i]);
            round = round + System.lineSeparator();
        }
        npRounds.setText(round);
    }

    public void showNamesOfPlayers(Notepad testNodepade) {
        TextView npPlayerNames;

        for (int i = 0; i < testNodepade.getPointsPerPlayerPerRound().length; i++) {
            switch (i) {
                case 0:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player1);
                    break;
                case 1:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player2);
                    break;
                case 2:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player3);
                    break;
                case 3:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player4);
                    break;
                case 4:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player5);
                    break;
                case 5:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player6);
                    break;

                default:
                    npPlayerNames = (TextView) dialog.findViewById(R.id.tv_player6);
            }
            npPlayerNames.setText(testNodepade.playerNamesList.get(i));
        }
    }

    //Vorhersage je Stich fix in Nodepad
    public void predictedTricks(Notepad testNodepade) {
        TextView npVorhersageplayer;

        for (int i = 0; i < testNodepade.getPointsPerPlayerPerRound().length; i++) {
            String testVorhersage = "";
            switch (i) {
                case 0:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks1);
                    break;
                case 1:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks2);
                    break;
                case 2:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks3);
                    break;
                case 3:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks4);
                    break;
                case 4:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks5);
                    break;
                case 5:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks6);
                    break;

                default:
                    npVorhersageplayer = (TextView) dialog.findViewById(R.id.tv_pointstricks6);
            }
            for (int j = 0; j < testNodepade.getPointsPerPlayerPerRound()[i].length; j++) {
                testVorhersage = testVorhersage + String.valueOf(testNodepade.getPointsPerPlayerPerRound()[i][j]);
                testVorhersage = testVorhersage + System.lineSeparator();
            }
            npVorhersageplayer.setText(testVorhersage);
        }
    }

    //Tatsächliche Stiche (switch) für Nodepad
    private void actualTricks(Notepad testNodepade) {
        String TricksPerRound = "Stiche per Runde";
        TextView npVorherSagePlayerTrue;
        TextView npTextChangePointsStiche;

        npTextChangePointsStiche = (TextView) dialog.findViewById(R.id.tv_stichepunkte);
        npTextChangePointsStiche.setText(TricksPerRound);
        testNodepade.testFillPointsPlayerround2();

        for (int i = 0; i < testNodepade.getPointsPerPlayerPerRound().length; i++) {
            String testPlayerpoints1 = "";
            switch (i) {
                case 0:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points1);
                    break;
                case 1:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points2);
                    break;
                case 2:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points3);
                    break;
                case 3:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points4);
                    break;
                case 4:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points5);
                    break;
                case 5:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points6);
                    break;

                default:
                    npVorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points6);
            }
            for (int j = 0; j < testNodepade.getPointsPerPlayerPerRound()[i].length; j++) {
                testPlayerpoints1 = testPlayerpoints1 + String.valueOf(testNodepade.getPointsPerPlayerPerRound()[i][j]);
                testPlayerpoints1 = testPlayerpoints1 + System.lineSeparator();
            }
            npVorherSagePlayerTrue.setText(testPlayerpoints1);
        }
    }

    //Erreichten Punkte für Nodepad (switch)
    private void actualPoints(Notepad testNodepade) {
        String PointsPerRound = "Punkte per Runde";
        TextView np_vorherSagePlayerTrue;
        TextView npTextChangePointsStiche;

        npTextChangePointsStiche = (TextView) dialog.findViewById(R.id.tv_stichepunkte);
        npTextChangePointsStiche.setText(PointsPerRound);
        testNodepade.testFillPointsPlayerround();

        for (int i = 0; i < testNodepade.getPointsPerPlayerPerRound().length; i++) {
            String testPlayerpoints1 = "";
            switch (i) {
                case 0:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points1);
                    break;
                case 1:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points2);
                    break;
                case 2:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points3);
                    break;
                case 3:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points4);
                    break;
                case 4:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points5);
                    break;
                case 5:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points6);
                    break;

                default:
                    np_vorherSagePlayerTrue = (TextView) dialog.findViewById(R.id.tv_points6);
            }
            for (int j = 0; j < testNodepade.getPointsPerPlayerPerRound()[i].length; j++) {
                testPlayerpoints1 = testPlayerpoints1 + String.valueOf(testNodepade.getPointsPerPlayerPerRound()[i][j]);
                testPlayerpoints1 = testPlayerpoints1 + System.lineSeparator();
            }
            np_vorherSagePlayerTrue.setText(testPlayerpoints1);
        }
    }

    @Override
    protected void onStop() {
        wizardClient.sendMessage(new LifecycleMessage("" + myPlayer.getName() + "left the game"));
        super.onStop();
    }

    @Override
    protected void onRestart() {
        wizardClient.sendMessage(new LifecycleMessage("" + myPlayer.getName() + "came back"));
        super.onRestart();
    }

    public void startCallback() {
        wizardClient.registerCallback(basemessage -> {
            if (basemessage instanceof StateMessage) {
                info("GAME_ACTIVITY: StateMessage received.");
                gameData.updateState((StateMessage) basemessage);
                if (gameData.getDealer() == (myPlayer.getConnectionID())) {
                    runOnUiThread(() ->
                            btnDeal.setEnabled(true));
                } else {
                    runOnUiThread(() ->
                            btnDeal.setEnabled(false));
                }

                info("Active Player: " + gameData.getActivePlayer() + ", Connection ID my Player: " + myPlayer.getConnectionID());

                if (((StateMessage) basemessage).isClearBetTricks()) {
                    runOnUiThread(() -> {
                        etVorhersage.setText("");
                        etVorhersage.setVisibility(View.INVISIBLE);
                    });
                }

                if (gameData.getActivePlayer() == (myPlayer.getConnectionID())) {
                    runOnUiThread(() ->
                            btnPlaySelectedCard.setEnabled(true));

                    if (gameData.getBetTricksCounter() < gameData.getScores().getTotalPointsPerPlayer().length) {
                        info("!!!!!!!!! Trickround: " + gameData.getBetTricksCounter() + " score size: " + gameData.getScores().getTotalPointsPerPlayer().length);
                        runOnUiThread(() -> {
                            etVorhersage.setEnabled(true);
                            etVorhersage.setVisibility(View.VISIBLE);
                            btnPlaySelectedCard.setEnabled(false);
                        });
                    } else {
                        runOnUiThread(() ->
                                btnPlaySelectedCard.setEnabled(true));
                    }

                } else {
                    runOnUiThread(() ->
                            btnPlaySelectedCard.setEnabled(false));
                }

                ArrayList<Card> cardsOnTable = gameData.getTable().getCards();
                runOnUiThread(() -> showTableCards(cardsOnTable));

            } else if (basemessage instanceof HandMessage) {
                info("GAME_ACTIVITY: Hand recieved.");
                gameData.setMyHand((HandMessage) basemessage);
                runOnUiThread(() ->
                        addCardsToSlideView(gameData.getMyHand().getCards()));

            } else if (basemessage instanceof NotePadMessage) {
                info("GAME_ACTIVITY: recieved scores!");
                gameData.setScores((NotePadMessage) basemessage);
                System.out.println(Arrays.deepToString(gameData.getScores().getPointsPerPlayerPerRound()));

            } else if (basemessage instanceof TextMessage) {
                String msg = ((TextMessage) basemessage).toString();
                runOnUiThread(() -> {
                    tvServerMsg.setText(msg);
                    addCardsToSlideView(gameData.getMyHand().getCards());
                });
            } else if (basemessage instanceof GoodbyeMessage) { // A player closed the app, so stop game and show current points as endresult
                info("GAME_ACTIVITY: Goodbye received.");
                runOnUiThread(() -> {
                    Intent intent = new Intent(this, EndscreenActivity.class);
                    startActivity(intent);
                });
            } else if (basemessage instanceof LifecycleMessage) {
                LifecycleMessage msg = (LifecycleMessage) basemessage;
                runOnUiThread(() -> tvServerMsg.setText(msg.getMsg()));

            } else if (basemessage instanceof ErrorMessage) {
                ErrorMessage msg = (ErrorMessage) basemessage;
                runOnUiThread(() -> {
                    etVorhersage.setEnabled(true);
                    etVorhersage.setText(msg.getError());
                });
                //et_vorhersage.setOnKeyListener((v, keyCode, keyEvent) -> enteredPrediction(keyCode, keyEvent));
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
    public void addCardsToSlideView(ArrayList<Card> ppPlayerCards) {
        //myHand.setCards(ppPlayerCards);
        sliderItems.clear(); //Clear wennn neue Carten von Server geschickt werden

        for (int i = 0; i < ppPlayerCards.size(); i++) {
            //umwandlung Color.Red, Value.Five in drawable picture alternativ (sliderItems.add(new SliderItem(R.drawable.red0eight));
            int id = getResources().getIdentifier(ppPlayerCards.get(i).getPictureFileId(), "drawable", getPackageName());

            if (id == 0) {//if the pictureID is false show Error Logo zero
                sliderItems.add(new SliderItem((R.drawable.z0error), ppPlayerCards.get(i)));
            } else {//show Card
                sliderItems.add(new SliderItem(id, ppPlayerCards.get(i)));
            }
        }
        viewPager2.setAdapter(sliderAdapter = new SliderAdapter(sliderItems, viewPager2));
    }

    private void showTableCards(ArrayList<Card> cards) {
        int cardID;
        //Issue removed critical dubplication "drawable"
        final String defTypedrawable = "drawable";
        switch (cards.size()) {
            case 6:
                cardID = getResources().getIdentifier(cards.get(5).getPictureFileId(), defTypedrawable, getPackageName());
                ivTable6.setImageResource(cardID);
                ivTable6.setVisibility(View.VISIBLE);
                break;
            case 5:
                cardID = getResources().getIdentifier(cards.get(4).getPictureFileId(), defTypedrawable, getPackageName());
                ivTable5.setImageResource(cardID);
                ivTable5.setVisibility(View.VISIBLE);
                break;
            case 4:
                cardID = getResources().getIdentifier(cards.get(3).getPictureFileId(), defTypedrawable, getPackageName());
                ivTable4.setImageResource(cardID);
                ivTable4.setVisibility(View.VISIBLE);
                break;
            case 3:
                cardID = getResources().getIdentifier(cards.get(2).getPictureFileId(), defTypedrawable, getPackageName());
                ivTable3.setImageResource(cardID);
                ivTable3.setVisibility(View.VISIBLE);
                break;
            case 2:
                cardID = getResources().getIdentifier(cards.get(1).getPictureFileId(), defTypedrawable, getPackageName());
                ivTable2.setImageResource(cardID);
                ivTable2.setVisibility(View.VISIBLE);
                break;
            case 1:
                cardID = getResources().getIdentifier(cards.get(0).getPictureFileId(), defTypedrawable, getPackageName());
                ivTable1.setImageResource(cardID);
                ivTable1.setVisibility(View.VISIBLE);
                break;
            case 0:
                ivTable1.setVisibility(View.INVISIBLE);
                ivTable2.setVisibility(View.INVISIBLE);
                ivTable3.setVisibility(View.INVISIBLE);
                ivTable4.setVisibility(View.INVISIBLE);
                ivTable5.setVisibility(View.INVISIBLE);
                ivTable6.setVisibility(View.INVISIBLE);
                break;
            default:
                info("Table Hand too short or too big! Something strange happened...");
        }
    }

    private void dealCards() {
        wizardClient.sendMessage(new ActionMessage(DEAL));
    }

    private void dealOnePlayerCardOnTable() {
        wizardClient.sendMessage(new CardMessage(sliderAdapter.getSelectedCard()));
    }

    public boolean enteredPrediction(int keycode, KeyEvent keyevent) {
        if (keyevent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER) {
            short betTricks = Short.parseShort(etVorhersage.getText().toString());
            etVorhersage.setEnabled(false);
            wizardClient.sendMessage(new NotePadMessage(gameData.getScores(), gameData.getActivePlayer(), betTricks));
            return true;
        } else {
            System.out.println("No NotePadMessage sent!");
            return false;
        }
    }

}