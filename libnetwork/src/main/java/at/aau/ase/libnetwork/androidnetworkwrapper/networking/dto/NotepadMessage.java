package at.aau.ase.libnetwork.androidnetworkwrapper.networking.dto;

import at.aau.ase.libnetwork.androidnetworkwrapper.networking.game.basic_classes.Notepad;

public class NotepadMessage {
    private Notepad notepad;

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
