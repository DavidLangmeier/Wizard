package at.aau.ase.wizard;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;

//Adapter provides the data model and responsible for rendering the views for the individual cell
//view holder - contains instances for a ll views that are filled by the data of the entry
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private int selectedICard=0;
    private Card selectedCard;


    SliderAdapter(List<SliderItem> sliderItems) {
        this.sliderItems = sliderItems;
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
        if(selectedICard==position){
            holder.imageView.setBackgroundColor(Color.parseColor("#fcdb19"));
        }else{
            holder.imageView.setBackgroundColor(Color.parseColor("#3c822f"));
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectetItem=selectedICard;
                selectedICard=position;
                notifyItemChanged(previousSelectetItem);
                holder.imageView.setBackgroundColor(Color.parseColor("#fcdb19"));
                selectedCard = sliderItems.get(position).getSelectedCard();
            }
        });
    }

    public Card getSelectedCard() {
        return selectedCard;
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
