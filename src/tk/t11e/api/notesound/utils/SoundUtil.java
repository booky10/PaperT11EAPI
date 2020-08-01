package tk.t11e.api.notesound.utils;

import tk.t11e.api.util.VersionHelper;

public class SoundUtil {

    public static String convert(String sound) {
        if (VersionHelper.aboveOr113() && sound.equals("BLOCK_NOTE_HARP"))
            return "BLOCK_NOTE_BLOCK_HARP";
        else if (VersionHelper.belowOr18())
            switch (sound) {
                case "BLOCK_NOTE_HARP":
                    return "NOTE_PIANO";
                case "BLOCK_NOTE_SNARE":
                    return "NOTE_SNARE_DRUM";
                case "BLOCK_NOTE_HAT":
                    return "NOTE_STICKS";
                case "BLOCK_NOTE_BASEDRUM":
                    return "NOTE_BASS_DRUM";
                case "BLOCK_NOTE_PLING":
                    return "NOTE_PLING";
                default:
                    return sound;
            }
        else
            return sound;
    }

    public static String convert(String sound, String replacedSound) {
        if (sound.equals("BLOCK_NOTE_BASS") && VersionHelper.aboveOr113())
            if (replacedSound.equals("NOTE_BASS"))
                return "BLOCK_NOTE_BLOCK_BASS";
            else if (replacedSound.equals("NOTE_BASS_GUITAR"))
                return "BLOCK_NOTE_BLOCK_GUITAR";
            else
                return sound;
        else if (VersionHelper.belowOr18())
            return convert(sound);
        else if (VersionHelper.aboveOr113())
            return replacedSound;
        else
            return sound;
    }
}
