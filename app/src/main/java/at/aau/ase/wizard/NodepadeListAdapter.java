package at.aau.ase.wizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NodepadeListAdapter extends ArrayAdapter<NodepadeTestPoints> {

    private Context testContest;
    int ubergabe;
//festestellen des Arraylist adapters
    public NodepadeListAdapter( Context context, int resource, ArrayList<NodepadeTestPoints> objects) {
        super(context, resource, objects);
        testContest = context;
        ubergabe=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       //informatin hohlen
        int runde= getItem(position).getRound();
        int punkte=getItem(position).getPoints();
        int vorhersage=getItem(position).getStiche();

        NodepadeTestPoints testAusgabe=new NodepadeTestPoints(runde,punkte,vorhersage);
        LayoutInflater inflatertest=LayoutInflater.from(testContest);
        convertView=inflatertest.inflate(ubergabe,parent,false);

        TextView tvrunde=(TextView) convertView.findViewById(R.id.tv_rounds);
        TextView tvpunkte=(TextView) convertView.findViewById(R.id.tv_pointsONE);
        TextView tvstiche=(TextView) convertView.findViewById(R.id.tv_sticheONE);
        tvrunde.setText(runde);
        tvpunkte.setText(punkte);
        tvstiche.setText(vorhersage);
        return convertView;
    }
}
