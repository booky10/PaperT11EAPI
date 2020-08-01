package tk.t11e.api.notesound;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import tk.t11e.api.notesound.utils.InstrumentUtil;
import tk.t11e.api.notesound.utils.MidiUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteSound {

    private File file;
    private Float tempo;
    private final List<Player> players;
    private NoteReceiver receiver;

    public NoteSound() {
        this.players = new ArrayList<Player>();
    }

    public void setMidiFile(final File file) {
        this.file = file;
    }

    public void setTempo(final float tempo) {
        this.tempo = tempo;
        if (this.receiver != null) {
            this.receiver.getSequencer().setTempoFactor(tempo);
        }
    }

    public boolean addPlayer(final Player player) {
        if (this.players.contains(player)) {
            return false;
        }
        this.players.add(player);
        return true;
    }

    public boolean removePlayer(final Player player) {
        if (!this.players.contains(player)) {
            return false;
        }
        this.players.remove(player);
        return true;
    }

    public long getTickLength() {
        if (this.receiver == null) {
            return -1L;
        }
        return this.receiver.getSequencer().getTickLength();
    }

    public long getMicrosecondsLength() {
        if (this.receiver == null) {
            return -1L;
        }
        return this.receiver.getSequencer().getMicrosecondLength();
    }

    public boolean setLoops(final int loops) {
        if (this.receiver == null) {
            return false;
        }
        this.receiver.getSequencer().setLoopCount(loops);
        return true;
    }

    public boolean togglePause() {
        if (this.receiver == null) {
            return false;
        }
        if (this.receiver.getSequencer().isRunning()) {
            this.receiver.getSequencer().stop();
            Bukkit.getPluginManager().callEvent((Event) new NoteSoundUpdateEvent(this, NoteState.PAUSE));
        } else {
            this.receiver.getSequencer().start();
            Bukkit.getPluginManager().callEvent((Event) new NoteSoundUpdateEvent(this, NoteState.CONTINUE));
        }
        return true;
    }

    public boolean stop() {
        return this.receiver != null && this.receiver.stop();
    }

    public boolean play() {
        if (this.receiver == null) {
            this.receiver = MidiUtil.playFile(this, this.file, this.tempo);
            return true;
        }
        if (!this.receiver.getSequencer().isRunning()) {
            this.receiver = MidiUtil.playFile(this, this.file, this.tempo);
            return true;
        }
        return false;
    }

    public boolean setTickPosition(final long tick) {
        if (this.receiver == null) {
            return false;
        }
        this.receiver.getSequencer().setTickPosition(tick);
        return true;
    }

    public boolean setMicrosecondPosition(final long tick) {
        if (this.receiver == null) {
            return false;
        }
        this.receiver.getSequencer().setMicrosecondPosition(tick);
        return true;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Set<String> getFamilyInstruments() {
        return InstrumentUtil.getFamilyInstruments(this.receiver);
    }

    public Set<String> getInstruments() {
        return InstrumentUtil.getInstruments(this.receiver);
    }

    public Set<Sound> getBukkitSounds() {
        return InstrumentUtil.getSounds(this.receiver);
    }

    public File getMidiFile() {
        return this.file;
    }
}
