package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_actions;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects.BaseMessage;

public class ActionMessage extends BaseMessage {
    private Action actionType;

    public  ActionMessage() {}

    public ActionMessage(Action actionType) {
        this.actionType = actionType;
    }

    public Action getActionType(){
        return actionType;
    }

    public void setActionType(Action actionType){
        this.actionType = actionType;
    }

    @Override
    public String toString(){
        return actionType.getActionName();
    }


}
