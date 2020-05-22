package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

public class LifecycleMessage extends BaseMessage {

    private String msg = null;

    public LifecycleMessage() {}

    public LifecycleMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
