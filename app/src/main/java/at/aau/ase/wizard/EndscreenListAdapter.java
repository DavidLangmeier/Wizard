package at.aau.ase.wizard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EndscreenListAdapter extends ArrayAdapter {

    private final Activity context;
    private final Integer[] imageID;
    private final List<String> players;
    private final int[] actualID;
    private int[][] totalPoints;

    public EndscreenListAdapter(Activity context, List<String> players, int[][] totalPoints, Integer[] imageID, int[] actualID) {

        super(context, R.layout.lobby_listview_row, players);

        this.context = context;
        this.players = players;
        this.imageID = imageID;
        this.actualID = actualID;
        this.totalPoints = totalPoints;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.endscreen_listview_row, null, false);

        TextView tvName = (TextView) rowView.findViewById(R.id.tvEndScreenListRow);
        ImageView ivIcon = (ImageView) rowView.findViewById(R.id.ivEndscreenListRow);
        String playersAndScores = players.get(position) + ": " + totalPoints[position][0] + "pts";
        tvName.setText(playersAndScores);
        ivIcon.setImageResource(imageID[actualID[position % imageID.length]]);

        return rowView;
    }
}