package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions;


public enum Action {

    /* add additional actions here */

    START(0, "Start game"),
    END(1, "End game"),
    READY(2, "I am ready"),
    DEAL(3, "Deal cards"),
    AGAIN(4, "Play again"),
    EXIT(5, "Exit game");


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
