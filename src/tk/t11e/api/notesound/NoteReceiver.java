package tk.t11e.api.notesound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import tk.t11e.api.notesound.utils.MidiUtil;
import tk.t11e.api.notesound.utils.SoundUtil;
import tk.t11e.api.notesound.utils.ToneUtil;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NoteReceiver implements Receiver {

    private final NoteSound sound;
    private Sequencer sequencer;
    private NoteSound noteSound;
    private final Map<Integer, Integer> channelPatches = new HashMap<>();
    private final Set<Integer> ids = new HashSet<>();

    public NoteReceiver(NoteSound sound) {
        this.sound = sound;
    }

    @Override
    public void send(MidiMessage message, long time) {
        if (message instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) message;
            Integer channel = shortMessage.getChannel();
            switch (shortMessage.getCommand()) {
                case 192:
                    Integer patch = shortMessage.getData1();
                    if (patch != 0)
                        ids.add(patch);
                    channelPatches.put(channel, patch);
                    break;
                case 144:
                    playNote(shortMessage);
                    break;
            }
        }
    }

    public void playNote(ShortMessage message) {
        if (144 != message.getCommand())
            return;
        float pitch = ToneUtil.convertToPitch(message).floatValue();
        float volume = message.getData2() / 127.0f;
        Integer id = channelPatches.get(message.getChannel());
        Sound instrument = Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_HARP"));

        if (id != null)
            instrument = MidiUtil.convertToInstrument(id);
        if (instrument != null)
            for (Player player : sound.getPlayers())
                player.playSound(player.getEyeLocation(), instrument, volume, pitch);
    }

    @Override
    public void close() {
        channelPatches.clear();
    }

    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void setNoteSound(NoteSound noteSound) {
        this.noteSound = noteSound;
    }

    public NoteSound getNoteSound() {
        return noteSound;
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public Boolean stop() {
        if (sequencer.isRunning()) {
            sequencer.stop();
            return true;
        } else
            return false;
    }
}
