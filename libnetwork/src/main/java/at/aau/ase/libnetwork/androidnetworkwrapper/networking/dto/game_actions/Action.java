package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions;


public enum Action {

    /* add additional actions here */

    START(0, "Start game"),
    END(1, "End game"),
    SHUFFLE(2, "Shuffle cards"),
    DEAL(3, "Deal cards");




    private final int actionCode;
    private final String actionName;


    public int getActionCode(){
        return actionCode;
    }

    public String getActionName(){
        return actionName;
    }

    Action(int actionCode, String actionName){
        this.actionCode = actionCode;
        this.actionName = actionName;
    }



}
