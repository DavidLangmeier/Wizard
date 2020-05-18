package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

public class ErrorMessage extends BaseMessage {

    private String error;

    public ErrorMessage() {}

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
