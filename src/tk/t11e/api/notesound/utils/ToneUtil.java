package tk.t11e.api.notesound.utils;

import org.bukkit.Note;

import javax.sound.midi.ShortMessage;

public class ToneUtil {

    private static final byte BASE_NOTE = new Note(1, Note.Tone.F, true).getId();
    private static final int MIDI_BASE_FSHARP = 54;

    public static Double convertToPitch(ShortMessage message) {
        double semitones = midiToNote(message).getId() - ToneUtil.BASE_NOTE;
        return Math.pow(2.0, semitones / 12.0);
    }

    private static Note midiToNote(ShortMessage message) {
        assert message.getCommand() == 144;
        int semitones = message.getData1() - 6;
        return new Note(semitones % 24);
    }
}
