package at.aau.ase.wizard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LobbyListAdapter extends ArrayAdapter {

    private final Activity context;
    private final Integer[] imageID;
    private final List<String> players;

    public LobbyListAdapter(Activity context, List<String> players, Integer[] imageID){

        super(context,R.layout.lobby_listview_row , players);

        this.context = context;
        this.players = players;
        this.imageID = imageID;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.lobby_listview_row, null,false);

        TextView tvName = (TextView) rowView.findViewById(R.id.etLobbyListRow);
        ImageView ivIcon = (ImageView) rowView.findViewById(R.id.ivLobbyListRow);

        tvName.setText(players.get(position));
        ivIcon.setImageResource(imageID[position%imageID.length]);

        return rowView;
    }
}
