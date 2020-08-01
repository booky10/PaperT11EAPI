package tk.t11e.api.notesound.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import tk.t11e.api.notesound.NoteReceiver;
import tk.t11e.api.notesound.NoteSound;
import tk.t11e.api.notesound.NoteSoundUpdateEvent;
import tk.t11e.api.notesound.NoteState;
import tk.t11e.api.util.VersionHelper;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MidiUtil {

    private static final List<NoteReceiver> sequencerList = new ArrayList<>();
    private static final Integer[] instrumentsOld = new Integer[]{0, 0, 0, 0, 0, 0, 0, 5, 6, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 2,
            5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
            1, 3, 1, 1, 1, 5, 1, 1, 1, 1, 1, 2, 4, 3};
    private static final Integer[] instrumentsNew = new Integer[]{0, 0, 0, 0, 0, 0, 0, 5, 6, 12, 9, 11, 11, 11,
            11, 7, 7, 7, 7, 7, 7, 7, 0, 5, 5, 10, 10, 10, 10, 10, 10, 10, 10, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5,
            5, 5, 5, 2, 5, 5, 5, 5, 0, 7, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
            8, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 13, 0, 0,
            0, 0, 0, 1, 7, 7, 3, 1, 1, 1, 5, 1, 10, 1, 1, 1, 2, 4, 3};

    public static void stop() {
        for (NoteReceiver receiver : MidiUtil.sequencerList)
            if (receiver.getSequencer().isRunning()) {
                receiver.getSequencer().stop();
                Bukkit.getPluginManager().callEvent(new NoteSoundUpdateEvent(receiver.getNoteSound(),
                        NoteState.STOP));
            }
    }

    private static NoteReceiver play(NoteSound sound, Sequence sequence, Float tempo) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer(false);
            NoteReceiver noteReceiver = new NoteReceiver(sound);

            sequencer.setSequence(sequence);
            sequencer.open();
            sequencer.setTempoFactor(tempo);
            sequencer.getTransmitter().setReceiver(noteReceiver);
            sequencer.start();

            Bukkit.getPluginManager().callEvent(new NoteSoundUpdateEvent(sound, NoteState.START));
            sequencer.addMetaEventListener(meta -> {
                if (meta.getType() == 47) {
                    Bukkit.getPluginManager().callEvent(new NoteSoundUpdateEvent(sound, NoteState.STOP));
                    MidiUtil.sequencerList.remove(noteReceiver);
                }
            });

            noteReceiver.setSequencer(sequencer);
            noteReceiver.setNoteSound(sound);
            MidiUtil.sequencerList.add(noteReceiver);
            return noteReceiver;
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    public static NoteReceiver playFile(NoteSound sound, File file, Float tempo) {
        try {
            return play(sound, MidiSystem.getSequence(file), tempo);
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    public static Sound convertToInstrument(Integer id) {
        Integer[] instruments = MidiUtil.instrumentsNew;
        if (VersionHelper.aboveOr113())
            instruments = MidiUtil.instrumentsOld;
        switch (instruments[id]) {
            case 1:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BASS", "NOTE_BASS"));
            case 2:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_SNARE", "BLOCK_NOTE_BLOCK_SNARE"));
            case 3:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_HAT", "BLOCK_NOTE_BLOCK_HAT"));
            case 4:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BASEDRUM", "BLOCK_NOTE_BLOCK_BASEDRUM"));
            case 5:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING"));
            case 6:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BASS", "NOTE_BASS_GUITAR"));
            case 7:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BELL", "BLOCK_NOTE_BLOCK_BELL"));
            case 8:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_FLUTE", "BLOCK_NOTE_BLOCK_FLUTE"));
            case 9:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_CHIME", "BLOCK_NOTE_BLOCK_CHIME"));
            case 10:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_GUITAR", "BLOCK_NOTE_BLOCK_GUITAR"));
            case 11:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_XYLOPHONE", "BLOCK_NOTE_BLOCK_XYLOPHONE"));
            case 12:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BLOCK_IRON_XYLOPHONE"));
            case 13:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BLOCK_BANJO"));
            case 14:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BLOCK_BIT"));
            case 15:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BLOCK_COW_BELL"));
            case 16:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_BLOCK_DIDGERIDOO"));
            default:
                return Sound.valueOf(SoundUtil.convert("BLOCK_NOTE_HARP", "BLOCK_NOTE_BLOCK_HARP"));
        }
    }
}
