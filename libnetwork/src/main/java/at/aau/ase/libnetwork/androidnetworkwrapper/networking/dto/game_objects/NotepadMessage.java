package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto.game_objects;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class NotepadMessage extends BaseMessage {
    private Notepad notepad;

    public NotepadMessage() {}

    public NotepadMessage(Notepad notepad){
        this.notepad = notepad;
    }

    public Notepad getNotepad(){
        return notepad;
    }

    public void setNotepad(Notepad notepad){
        this.notepad = notepad;
    }


}
