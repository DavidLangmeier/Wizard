package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

public class TextMessage extends BaseMessage {

    public TextMessage() {}

    public TextMessage(String text) {
        this.text = text;
    }

    public String text;

    @Override
    public String toString() {
        return String.format("Server: %s", text);
    }
}
