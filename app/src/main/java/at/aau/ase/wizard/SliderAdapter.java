package at.aau.ase.wizard;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import static com.esotericsoftware.minlog.Log.debug;

//Adapter provides the data model and responsible for rendering the views for the individual cell
//view holder - contains instances for a ll views that are filled by the data of the entry
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;
    private int selectedICard = 0;



    SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_card_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));

        //Hintergrundfarbe bei Auswählen Bild  GELB
        if (selectedICard == position) {
            holder.imageView.setBackgroundColor(Color.parseColor("#fcdb19"));
        } else {
            holder.imageView.setBackgroundColor(Color.parseColor("#3c822f"));
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectetItem = selectedICard;
                selectedICard = position;
                notifyItemChanged(previousSelectetItem);
                holder.imageView.setBackgroundColor(Color.parseColor("#fcdb19"));
                //debug("test");
                Card currentPlayingCard= sliderItems.get(position).getGameCard();
                onItemClickCard(currentPlayingCard);

 //--- Carte die Angeklickt wurde für Ausspielen Spiel-------!!!--------------------------------------------
                String playingOutCard =currentPlayingCard.getValue()+" "+currentPlayingCard.getColor();
                Log.i("test", "Playing out Card"+playingOutCard);

            }
        });
    }

    public void onItemClickCard(Card selectedCard)
    {

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;


        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderItem sliderItem) {
            imageView.setImageResource(sliderItem.getImage());
        }
    }
}
