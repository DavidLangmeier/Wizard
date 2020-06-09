package at.aau.ase.wizard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.ActionMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CardMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.CheatMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.EndscreenMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.ErrorMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.HandMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.LifecycleMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.NotePadMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.StateMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.TextMessage;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Player;

import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.END;
import static at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions.Action.READY;
import static com.esotericsoftware.minlog.Log.debug;
import static com.esotericsoftware.minlog.Log.info;


public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private Button btnPlaySelectedCard;
    private Button btnShowNotepad;
    private TextView tvTrumpColor;
    private TextView tvActivePlayer1;
    private TextView tvActivePlayer2;
    private TextView tvActivePlayer3;
    private TextView tvActivePlayer4;
    private TextView tvActivePlayer5;
    private TextView tvActivePlayer6;
    private ImageView ivTable1;
    private ImageView ivTable2;
    private ImageView ivTable3;
    private ImageView ivTable4;
    private ImageView ivTable5;
    private ImageView ivTable6;
    private ViewPager2 viewPager2;
    private WizardClient wizardClient;
    private List<SliderItem> sliderItems = new ArrayList<>(); //Zeigt scrollHand
    private Player myPlayer;
    private GameData gameData;
    private SliderAdapter sliderAdapter; //to access player Card from Scrollhand later
    private Dialog dialog;
    private TextView tvServerMsg;
    private EditText etPrediction;
    private List<String> playersOnline = new ArrayList<>();
    private MediaPlayer mp3;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float lightOld = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String s1 = getIntent().getStringExtra("myPlayer");
        myPlayer = new Gson().fromJson(s1, Player.class);
        String s2 = getIntent().getStringExtra("gameData");
        gameData = new Gson().fromJson(s2, GameData.class);
        String s3 = getIntent().getStringExtra("playersOnline");
        playersOnline = new Gson().fromJson(s3, List.class);

        wizardClient = WizardClient.getInstance();
        startCallback();
        info("@GAME_ACTIVITY: My Playername=" + myPlayer.getName() + ", connectionID=" + myPlayer.getConnectionID());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        btnShowNotepad = findViewById(R.id.game_btn_showNodepad);
        btnShowNotepad.setOnClickListener(this::showNodepad);
        btnShowNotepad.setEnabled(false);

        btnPlaySelectedCard = findViewById(R.id.play_Card);
        btnPlaySelectedCard.setOnClickListener(v -> dealOnePlayerCardOnTable());
        btnPlaySelectedCard.setEnabled(false);
        dialog = new Dialog(this);
        tvServerMsg = findViewById(R.id.game_textView_serverMsg);
        tvTrumpColor = findViewById(R.id.tv_TrumpColor);

        etPrediction = findViewById(R.id.etn_Vorhersage);
        etPrediction.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPrediction.setVisibility(View.INVISIBLE);

        ivTable1 = findViewById(R.id.tableCard1);
        ivTable2 = findViewById(R.id.tableCard2);
        ivTable3 = findViewById(R.id.tableCard3);
        ivTable4 = findViewById(R.id.tableCard4);
        ivTable5 = findViewById(R.id.tableCard5);
        ivTable6 = findViewById(R.id.tableCard6);

        tvActivePlayer1 = findViewById(R.id.tv_p1);
        tvActivePlayer2 = findViewById(R.id.tv_p2);
        tvActivePlayer3 = findViewById(R.id.tv_p3);
        tvActivePlayer4 = findViewById(R.id.tv_p4);
        tvActivePlayer5 = findViewById(R.id.tv_p5);
        tvActivePlayer6 = findViewById(R.id.tv_p6);

        tvActivePlayer4.setVisibility(View.INVISIBLE);
        tvActivePlayer5.setVisibility(View.INVISIBLE);
        tvActivePlayer6.setVisibility(View.INVISIBLE);
        runOnUiThread(this::setPlayerViews); // fills player names in correct Textview

        viewPager2 = findViewById(R.id.viewPagerImageSlieder);
        mp3 = MediaPlayer.create(this, R.raw.karte0runterlegen);

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

        etPrediction.setOnKeyListener((v, keyCode, keyEvent) -> enteredPrediction(keyCode, keyEvent));
    }

    public void showNodepad(View v) {
        //View wird im XML aufgerufen
        TextView txtclose;
        mp3.start();
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


        Notepad gameDataScores = gameData.getScores();
        info("....................getPointsPerPlayerPerRound......................" + Arrays.deepToString(gameData.getScores().getPointsPerPlayerPerRound()));
        info("....................getBetTricksPerPlayerPerRound......................" + Arrays.deepToString(gameData.getScores().getBetTricksPerPlayerPerRound()));
        info(".....................getTookTricksPerPlayerPerRound....................." + Arrays.deepToString(gameData.getScores().getTookTricksPerPlayerPerRound()));
        info("......................playerNamesList...................." + (gameData.getScores().getPlayerNamesList()));

        info(".....................getRoundsLeft...................................." + (gameData.getRoundsLeft()));
        info(".....................ActivePlayer.................................." + (gameData.getActivePlayer()));
        info(".....................Notepad.getRound................................" + (Notepad.getRound()));
        info("......................getTotalPointsPerPlayer()....................." + Arrays.deepToString(gameData.getScores().getTotalPointsPerPlayer()));
        //----------------------switch-------------------------

        swChangeViewPointsStiche = (Switch) dialog.findViewById(R.id.sw_switch1);
        int actualRound = findOutRound(gameDataScores);
        swChangeViewPointsStiche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //wenn Switch isCheck fill with Points
                    actualPoints(gameDataScores, actualRound);
                } else {
                    actualTricks(gameDataScores, actualRound);
                }
            }
        });

        //Befüllung der Runden Zahl
        fillRoundsNumber(gameDataScores);
        //Player Namen Nodepad Befüllung
        showNamesOfPlayers(playersOnline);
        //Aktuelle Punkte Nodepad Befüllung
        actualPoints(gameDataScores, actualRound);
        //Vorhersage Stiche Nodepad Befüllung
        predictedTricks(gameDataScores, actualRound);
        //Summe totalPunkte Befüllung
        totalPointsPlayer(gameDataScores);

        dialog.show();
    }

    public void fillRoundsNumber(Notepad gameDataScores) {
        int rundenDisplay;
        int kartenAnzahl;
        TextView npRounds;
        npRounds = (TextView) dialog.findViewById(R.id.tv_rounds);

        kartenAnzahl = 60;
        rundenDisplay = kartenAnzahl / (gameDataScores.getTotalPointsPerPlayer().length);

        //60/3=20  60/4=15  60/5=12 60 /6=10

        //Befüllung mit Werten Array
        short[] roundsArray = new short[rundenDisplay + 1];
        for (int i = 0; i < roundsArray.length; i++) {
            roundsArray[i] = (short) i;
        }
        //Anzeige in Textfeld
        StringBuilder round = new StringBuilder();
        for (int i = 1; i < roundsArray.length; i++) {
            round.append(" ");
            round.append(String.valueOf(roundsArray[i]));
            round.append(System.lineSeparator());
        }
        npRounds.setText(round.toString());
    }

    public void showNamesOfPlayers(List<String> playersOnline) {
        TextView npPlayerNames;

        for (int i = 0; i < playersOnline.size(); i++) {
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
            npPlayerNames.setText(playersOnline.get(i));
        }
    }

    public void totalPointsPlayer(Notepad testNodepade) {
        TextView npTotalPoints;

        for (int i = 0; i < testNodepade.getTotalPointsPerPlayer().length; i++) {
            String npTotalPointsAnzeige = "";
            switch (i) {
                case 0:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe1);
                    break;
                case 1:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe2);
                    break;
                case 2:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe3);
                    break;
                case 3:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe4);
                    break;
                case 4:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe5);
                    break;
                case 5:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe6);
                    break;

                default:
                    npTotalPoints = (TextView) dialog.findViewById(R.id.tv_summe6);
            }
            StringBuilder bld = new StringBuilder();
            for (int j = 0; j < testNodepade.getTotalPointsPerPlayer()[i].length; j++) {
                bld.append(" ");
                bld.append(npTotalPointsAnzeige + String.valueOf(testNodepade.getTotalPointsPerPlayer()[i][j]));
            }

            npTotalPoints.setText(bld.toString());
        }
    }

    //Vorhersage je Stich fix in Nodepad
    public void predictedTricks(Notepad testNodepade, int actualRound) {
        TextView npVorhersageplayer;

        for (int i = 0; i < testNodepade.getBetTricksPerPlayerPerRound().length; i++) {
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
            StringBuilder testVorhersage = new StringBuilder();
            for (int j = 0; j < testNodepade.getBetTricksPerPlayerPerRound()[i].length; j++) {
                testVorhersage.append("  ");
                if (actualRound > j) {
                    testVorhersage.append(String.valueOf(testNodepade.getBetTricksPerPlayerPerRound()[i][j]));
                    testVorhersage.append(System.lineSeparator());
                } else {
                    testVorhersage.append(String.valueOf(" "));
                    testVorhersage.append(System.lineSeparator());
                }
            }
            npVorhersageplayer.setText(testVorhersage.toString());
        }
    }

    //Tatsächliche Stiche (switch) für Nodepad

    private void actualTricks(Notepad testNodepade,int actualRound) {
        String tricksPerRound = "Tricks per round";

        TextView npVorherSagePlayerFalse;
        TextView npTextChangePointsStiche;

        npTextChangePointsStiche = (TextView) dialog.findViewById(R.id.tv_stichepunkte);
        npTextChangePointsStiche.setText(tricksPerRound);

        for (int i = 0; i < testNodepade.getTookTricksPerPlayerPerRound().length; i++) {
            switch (i) {
                case 0:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points1);
                    break;
                case 1:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points2);
                    break;
                case 2:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points3);
                    break;
                case 3:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points4);
                    break;
                case 4:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points5);
                    break;
                case 5:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points6);
                    break;

                default:
                    npVorherSagePlayerFalse = (TextView) dialog.findViewById(R.id.tv_points6);
            }
            StringBuilder testPlayerpoints1 = new StringBuilder();
            for (int j = 0; j < testNodepade.getTookTricksPerPlayerPerRound()[i].length; j++) {
                testPlayerpoints1.append("  ");

                if(actualRound-1>j) {

                

                    testPlayerpoints1.append(String.valueOf(testNodepade.getTookTricksPerPlayerPerRound()[i][j]));
                    testPlayerpoints1.append(System.lineSeparator());
                } else {
                    testPlayerpoints1.append(String.valueOf(" "));
                    testPlayerpoints1.append(System.lineSeparator());
                }

            }
            npVorherSagePlayerFalse.setText(testPlayerpoints1.toString());
            npVorherSagePlayerFalse.setTextColor(Color.YELLOW);
        }
    }

    public int findOutRound(Notepad testNodepade) {
        return testNodepade.getPointsPerPlayerPerRound()[0].length - gameData.getRoundsLeft();
    }

    //Erreichten Punkte für Nodepad (switch)
    private void actualPoints(Notepad testNodepade, int actualRound) {
        String pointsPerRound = "Points per round";
        TextView npVorherSagePlayerTrue;
        TextView npTextChangePointsStiche;

        npTextChangePointsStiche = (TextView) dialog.findViewById(R.id.tv_stichepunkte);
        npTextChangePointsStiche.setText(pointsPerRound);


        for (int i = 0; i < testNodepade.getPointsPerPlayerPerRound().length; i++) {
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
            StringBuilder testPlayerpoints1 = new StringBuilder();
            for (int j = 0; j < testNodepade.getPointsPerPlayerPerRound()[i].length; j++) {
                testPlayerpoints1.append("  ");

                if(actualRound-1>j) {

               

                    testPlayerpoints1.append(String.valueOf(testNodepade.getPointsPerPlayerPerRound()[i][j]));
                    testPlayerpoints1.append(System.lineSeparator());
                } else {
                    testPlayerpoints1.append(String.valueOf(" "));
                    testPlayerpoints1.append(System.lineSeparator());
                }

            }
            npVorherSagePlayerTrue.setText(testPlayerpoints1);
            npVorherSagePlayerTrue.setTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onStop() {
        if (wizardClient != null) {
            wizardClient.sendMessage(new LifecycleMessage("" + myPlayer.getName() + " left the game"));
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        wizardClient.sendMessage(new LifecycleMessage("" + myPlayer.getName() + " came back"));
        super.onRestart();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        info("Sensor accurracy changed!");
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float light = event.values[0];

        if (light <= SensorManager.LIGHT_NO_MOON / 10 && lightOld - light > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.cheat_dialog_title);
            List<String> possibleCheatersToCheckList = new ArrayList<>();
            for (String s : playersOnline) {
                if (!s.equals(myPlayer.getName()))
                    possibleCheatersToCheckList.add(s);
            }
            builder.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, possibleCheatersToCheckList), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String selected = possibleCheatersToCheckList.get(which);
                    info("Selected " + selected);
                    wizardClient.sendMessage(new CheatMessage(selected, myPlayer));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        lightOld = light;
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void startCallback() {
        wizardClient.registerCallback(basemessage -> {
            if (basemessage instanceof StateMessage) {
                info("GAME_ACTIVITY: StateMessage received.");
                gameData.updateState((StateMessage) basemessage);
                if (!btnShowNotepad.isEnabled())
                    runOnUiThread(() -> btnShowNotepad.setEnabled(true));
                if (gameData.getRoundsLeft() >= 1) {
                    tvTrumpColor.setText("Trump: " + gameData.getTrump().getColorName());
                    switch (gameData.getTrump().getColorName()) {
                        case "Blue":
                            tvTrumpColor.setTextColor(Color.BLUE);
                            break;
                        case "Red":
                            tvTrumpColor.setTextColor(Color.RED);
                            break;
                        case "Green":
                            tvTrumpColor.setTextColor(Color.GREEN);
                            break;
                        case "Yellow":
                            tvTrumpColor.setTextColor(Color.YELLOW);
                            break;
                        default:
                            tvTrumpColor.setTextColor(Color.BLACK);
                    }
                } else {
                    tvTrumpColor.setText("Trump: No Trump");
                    tvTrumpColor.setTextColor(Color.BLACK);
                }

                info("Active Player: " + gameData.getActivePlayer() + ", Connection ID my Player: " + myPlayer.getConnectionID());

                if (((StateMessage) basemessage).isClearBetTricks()) {
                    runOnUiThread(() -> {
                        etPrediction.setText("");
                        etPrediction.setVisibility(View.INVISIBLE);
                    });
                }

                if (gameData.getActivePlayer() == (myPlayer.getConnectionID())) {
                    runOnUiThread(() -> btnPlaySelectedCard.setEnabled(true));

                    if (gameData.getBetTricksCounter() < gameData.getScores().getTotalPointsPerPlayer().length) {
                        info("!!!!!!!!! Trickround: " + gameData.getBetTricksCounter() + " score size: " + gameData.getScores().getTotalPointsPerPlayer().length);
                        runOnUiThread(() -> {
                            etPrediction.setHint("Bet tricks:");
                            etPrediction.setEnabled(true);
                            etPrediction.setVisibility(View.VISIBLE);
                            btnPlaySelectedCard.setEnabled(false);
                            runOnUiThread(this::setTvActivePlayer);
                        });
                    } else {
                        runOnUiThread(() ->
                                btnPlaySelectedCard.setEnabled(true));
                    }

                } else {
                    runOnUiThread(() ->
                            btnPlaySelectedCard.setEnabled(false));
                }

                List<Card> cardsOnTable = gameData.getTable().getCards();
                runOnUiThread(() -> showTableCards(cardsOnTable));
                runOnUiThread(this::setTvActivePlayer);

            } else if (basemessage instanceof HandMessage) {
                info("GAME_ACTIVITY: Hand recieved.");
                gameData.setMyHand((HandMessage) basemessage);
                runOnUiThread(() ->
                        addCardsToSlideView(gameData.getMyHand().getCards()));

            } else if (basemessage instanceof NotePadMessage) {
                info("GAME_ACTIVITY: recieved scores!");
                gameData.setScores((NotePadMessage) basemessage);
                info(Arrays.deepToString(gameData.getScores().getPointsPerPlayerPerRound()));

            } else if (basemessage instanceof TextMessage) {
                String msg = ((TextMessage) basemessage).toString();
                runOnUiThread(() -> {
                    tvServerMsg.setText(msg);
                    addCardsToSlideView(gameData.getMyHand().getCards());
                });
            } else if (basemessage instanceof ActionMessage) { // A player closed the app, so stop game and show current points as endresult
                if (((ActionMessage) basemessage).getActionType() == END) {
                    info("GAME_ACTIVITY: END received. - Trying to start endscreen activity.");
                    gameData.getScores().setPlayerNamesList(playersOnline); // to access in Endscreen
                    Notepad endscreenScores = gameData.getScores();
                    debug("------------+++++++++++++++--------------" + Arrays.deepToString(endscreenScores.getTotalPointsPerPlayer()));
                    wizardClient.sendMessage(new EndscreenMessage(endscreenScores)); //prepare for Endscreen
                }
                //START endscreen activity
            } else if (basemessage instanceof EndscreenMessage) {
                EndscreenMessage msg = (EndscreenMessage) basemessage;
                Intent intent = new Intent(this, EndscreenActivity.class);
                wizardClient.deregisterCallback();
                intent.putExtra("endscreenScores", new Gson().toJson(msg.getScores()));
                intent.putExtra("sortedIconID", (new Gson()).toJson(msg.getImageID()));
                intent.putExtra("playersOnline", (new Gson()).toJson(playersOnline));
                intent.putExtra("myPlayer", (new Gson()).toJson(myPlayer));
                startActivity(intent);
            } else if (basemessage instanceof LifecycleMessage) {
                LifecycleMessage msg = (LifecycleMessage) basemessage;
                runOnUiThread(() -> tvServerMsg.setText(msg.getMsg()));
            } else if (basemessage instanceof CheatMessage) {
                CheatMessage msg = (CheatMessage) basemessage;
                runOnUiThread(() -> tvServerMsg.setText(msg.getMessage()));
            } else if (basemessage instanceof ErrorMessage) {
                ErrorMessage msg = (ErrorMessage) basemessage;
                runOnUiThread(() -> {
                    etPrediction.setEnabled(true);
                    etPrediction.setText("");
                    errorAnimation(msg);
                });
            }

        });
        wizardClient.sendMessage(new ActionMessage(READY));
        info("GAME_ACTIVITY: READY sent.");
    }

    //------------Methode SPIELKARTEN vom Server Anzeigen in spielhand//--------------------------
    public void addCardsToSlideView(List<Card> ppPlayerCards) {
        final String defTypedrawable = "drawable";


        sliderItems.clear(); //Clear wennn neue Carten von Server geschickt werden

        for (int i = 0; i < ppPlayerCards.size(); i++) {

            int id = getResources().getIdentifier(ppPlayerCards.get(i).getPictureFileId(), defTypedrawable, getPackageName());

            if (id == 0) {//if the pictureID is false show Error Logo zero
                sliderItems.add(new SliderItem((R.drawable.z0error), ppPlayerCards.get(i)));
            } else {//show Card
                sliderItems.add(new SliderItem(id, ppPlayerCards.get(i)));
            }
        }
        sliderAdapter = new SliderAdapter(sliderItems);
        viewPager2.setAdapter(sliderAdapter);
    }

    private void showTableCards(List<Card> cards) {
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

    private void setPlayerViews() {
        switch (playersOnline.size()) {
            case 6:
                tvActivePlayer6.setText(playersOnline.get(5));
                tvActivePlayer6.setVisibility(View.VISIBLE);
                break;
            case 5:
                tvActivePlayer5.setText(playersOnline.get(4));
                tvActivePlayer5.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvActivePlayer4.setText(playersOnline.get(3));
                tvActivePlayer4.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvActivePlayer3.setText(playersOnline.get(2));
                tvActivePlayer2.setText(playersOnline.get(1));
                tvActivePlayer1.setText(playersOnline.get(0));
                break;

            default:
                info("GAME_ACTIVITY: No Players active.");
        }
    }

    private void showActivePlayers(TextView activePlayer) {
        tvActivePlayer1.setEnabled(false);
        tvActivePlayer2.setEnabled(false);
        tvActivePlayer3.setEnabled(false);
        tvActivePlayer4.setEnabled(false);
        tvActivePlayer5.setEnabled(false);
        tvActivePlayer6.setEnabled(false);
        activePlayer.setEnabled(true);
    }

    private void setTvActivePlayer() {
        if (gameData.getActivePlayerIndex() == 0) {
            showActivePlayers(tvActivePlayer1);
        } else if (gameData.getActivePlayerIndex() == 1) {
            showActivePlayers(tvActivePlayer2);
        } else if (gameData.getActivePlayerIndex() == 2) {
            showActivePlayers(tvActivePlayer3);
        } else if (gameData.getActivePlayerIndex() == 3) {
            showActivePlayers(tvActivePlayer4);
        } else if (gameData.getActivePlayerIndex() == 4) {
            showActivePlayers(tvActivePlayer5);
        } else if (gameData.getActivePlayerIndex() == 5) {
            showActivePlayers(tvActivePlayer6);
        } else {
            info("GAME_ACTIVITY: No active Player to show.");
        }
    }

    private void dealOnePlayerCardOnTable() {
        wizardClient.sendMessage(new CardMessage(sliderAdapter.getSelectedCard()));
    }

    public boolean enteredPrediction(int keycode, KeyEvent keyevent) {
        if (keyevent.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_ENTER && checkIfEmpty(etPrediction)) {
            short betTricks = Short.parseShort(etPrediction.getText().toString());
            etPrediction.setEnabled(false);
            wizardClient.sendMessage(new NotePadMessage(gameData.getScores(), gameData.getActivePlayer(), betTricks));
            return true;
        } else {
            info("No NotePadMessage sent!");
            return false;
        }
    }

    boolean checkIfEmpty(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            ErrorMessage msg = new ErrorMessage("Enter bet!");
            errorAnimation(msg);
            return false;
        } else {
            return true;
        }
    }

    void errorAnimation(ErrorMessage msg) {
        Animation shake = AnimationUtils.loadAnimation(GameActivity.this, R.anim.shake);
        etPrediction.startAnimation(shake);
        etPrediction.setHint(msg.getError());
    }

}