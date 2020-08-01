package tk.t11e.api.notesound;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NoteSoundUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final NoteSound noteSound;
    private final NoteState noteState;

    public NoteSoundUpdateEvent(NoteSound noteSound, NoteState noteState) {
        this.noteSound = noteSound;
        this.noteState = noteState;
    }

    public NoteState getNoteState() {
        return noteState;
    }

    public NoteSound getNoteSound() {
        return noteSound;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
