package at.aau.ase.wizard;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Card;


public class SliderItem {


    private int image;
    private Card gameCard;

    SliderItem(int image, Card gameCard) {
        this.image = image;
        this.gameCard=gameCard;
    }

    public int getImage(){
        return image;
    }
    public Card getGameCard() {
        return gameCard;
    }


}
