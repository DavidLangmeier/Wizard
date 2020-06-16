package at.aau.ase.wizard;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;

public class SliderItem {


    private int image;
    private Card selectedCard;


    SliderItem(int image, Card gameCard) {
        this.image = image;
        this.selectedCard = gameCard;
    }

    public int getImage(){
        return image;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }
}
