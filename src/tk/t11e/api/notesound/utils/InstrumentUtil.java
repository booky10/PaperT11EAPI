package tk.t11e.api.notesound.utils;

import org.bukkit.Sound;
import tk.t11e.api.notesound.NoteReceiver;

import java.util.HashSet;
import java.util.Set;

public class InstrumentUtil {

    public static Set<String> getFamilyInstruments(NoteReceiver receiver) {
        Set<String> instruments = new HashSet<>();
        if (receiver != null)
            for (Integer id : receiver.getIds()) {
                String instrument = getFamilyName(id);
                instruments.add(instrument);
            }
        return instruments;
    }

    public static Set<String> getInstruments(NoteReceiver receiver) {
        Set<String> instruments = new HashSet<>();
        if (receiver != null)
            for (Integer id : receiver.getIds()) {
                String instrument = getName(id);
                instruments.add(instrument);
            }
        return instruments;
    }

    public static Set<Sound> getSounds(NoteReceiver receiver) {
        Set<Sound> sounds = new HashSet<>();
        if (receiver != null)
            for (Integer id : receiver.getIds()) {
                Sound sound = MidiUtil.convertToInstrument(id);
                sounds.add(sound);
            }
        return sounds;
    }

    public static String getFamilyName(Integer id) {
        if (id == 0)
            return "Unknown";
        else if (id >= 1 && id <= 8)
            return "Piano";
        else if (id >= 9 && id <= 16)
            return "Chromatic Percussion";
        else if (id >= 17 && id <= 24)
            return "Organ";
        else if (id >= 25 && id <= 32)
            return "Guitar";
        else if (id >= 33 && id <= 40)
            return "Bass";
        else if (id >= 41 && id <= 48)
            return "Strings";
        else if (id >= 49 && id <= 56)
            return "Ensemble";
        else if (id >= 57 && id <= 64)
            return "Brass";
        else if (id >= 65 && id <= 72)
            return "Reed";
        else if (id >= 73 && id <= 80)
            return "Pipe";
        else if (id >= 81 && id <= 88)
            return "Synth Lead";
        else if (id >= 89 && id <= 96)
            return "Synth Pad";
        else if (id >= 97 && id <= 104)
            return "Synth Effects";
        else if (id >= 105 && id <= 112)
            return "Ethnic";
        else if (id >= 113 && id <= 120)
            return "Percussive";
        else if (id >= 121 && id <= 128)
            return "Sound Effects";
        else
            return null;
    }

    public static String getName(Integer id) {
        switch (id) {
            case 1:
                return "Acoustic Grand Piano";
            case 2:
                return "Bright Acoustic Piano";
            case 3:
                return "Electric Grand Piano";
            case 4:
                return "Honky-tonk Piano";
            case 5:
                return "Electric Piano 1";
            case 6:
                return "Electric Piano 2";
            case 7:
                return "Harpsichord";
            case 8:
                return "Clavi";
            case 9:
                return "Celesta";
            case 10:
                return "Glockenspiel";
            case 11:
                return "Music Box";
            case 12:
                return "Vibraphone";
            case 13:
                return "Marimba";
            case 14:
                return "Xylophone";
            case 15:
                return "Tubular Bells";
            case 16:
                return "Dulcimer";
            case 17:
                return "Drawbar Organ";
            case 18:
                return "Percussive Organ";
            case 19:
                return "Rock Organ";
            case 20:
                return "Church Organ";
            case 21:
                return "Reed Organ";
            case 22:
                return "Accordion";
            case 23:
                return "Harmonica";
            case 24:
                return "Tango Accordion";
            case 25:
                return "Acoustic Guitar (nylon)";
            case 26:
                return "Acoustic Guitar (steel)";
            case 27:
                return "Electric Guitar (jazz)";
            case 28:
                return "Electric Guitar (clean)";
            case 29:
                return "Electric Guitar (muted)";
            case 30:
                return "Overdriven Guitar";
            case 31:
                return "Distortion Guitar";
            case 32:
                return "Guitar harmonics";
            case 33:
                return "Acoustic Bass";
            case 34:
                return "Electric Bass (finger)";
            case 35:
                return "Electric Bass (pick)";
            case 36:
                return "Fretless Bass";
            case 37:
                return "Slap Bass 1";
            case 38:
                return "Slap Bass 2";
            case 39:
                return "Synth Bass 1";
            case 40:
                return "Synth Bass 2";
            case 41:
                return "Violin";
            case 42:
                return "Viola";
            case 43:
                return "Cello";
            case 44:
                return "Contrabass";
            case 45:
                return "Tremolo Strings";
            case 46:
                return "Pizzicato Strings";
            case 47:
                return "Orchestral Harp";
            case 48:
                return "Timpani";
            case 49:
                return "String Ensemble 1";
            case 50:
                return "String Ensemble 2";
            case 51:
                return "SynthStrings 1";
            case 52:
                return "SynthStrings 2";
            case 53:
                return "Choir Aahs";
            case 54:
                return "Voice Oohs";
            case 55:
                return "Synth Voice";
            case 56:
                return "Orchestra Hit";
            case 57:
                return "Trumpet";
            case 58:
                return "Trombone";
            case 59:
                return "Tuba";
            case 60:
                return "Muted Trumpet";
            case 61:
                return "French Horn";
            case 62:
                return "Brass Section";
            case 63:
                return "SynthBrass 1";
            case 64:
                return "SynthBrass 2";
            case 65:
                return "Soprano Sax";
            case 66:
                return "Alto Sax";
            case 67:
                return "Tenor Sax";
            case 68:
                return "Baritone Sax";
            case 69:
                return "Oboe";
            case 70:
                return "English Horn";
            case 71:
                return "Bassoon";
            case 72:
                return "Clarinet";
            case 73:
                return "Piccolo";
            case 74:
                return "Flute";
            case 75:
                return "Recorder";
            case 76:
                return "Pan Flute";
            case 77:
                return "Blown Bottle";
            case 78:
                return "Shakuhachi";
            case 79:
                return "Whistle";
            case 80:
                return "Ocarina";
            case 81:
                return "Lead 1 (square)";
            case 82:
                return "Lead 2 (sawtooth)";
            case 83:
                return "Lead 3 (calliope)";
            case 84:
                return "Lead 4 (chiff)";
            case 85:
                return "Lead 5 (charang)";
            case 86:
                return "Lead 6 (voice)";
            case 87:
                return "Lead 7 (fifths)";
            case 88:
                return "Lead 8 (bass + lead)";
            case 89:
                return "Pad 1 (new age)";
            case 90:
                return "Pad 2 (warm)";
            case 91:
                return "Pad 3 (polysynth)";
            case 92:
                return "Pad 4 (choir)";
            case 93:
                return "Pad 5 (bowed)";
            case 94:
                return "Pad 6 (metallic)";
            case 95:
                return "Pad 7 (halo)";
            case 96:
                return "Pad 8 (sweep)";
            case 97:
                return "FX 1 (rain)";
            case 98:
                return "FX 2 (soundtrack)";
            case 99:
                return "FX 3 (crystal)";
            case 100:
                return "FX 4 (atmosphere)";
            case 101:
                return "FX 5 (brightness)";
            case 102:
                return "FX 6 (goblins)";
            case 103:
                return "FX 7 (echoes)";
            case 104:
                return "FX 8 (sci-fi)";
            case 105:
                return "Sitar";
            case 106:
                return "Banjo";
            case 107:
                return "Shamisen";
            case 108:
                return "Koto";
            case 109:
                return "Kalimba";
            case 110:
                return "Bag pipe";
            case 111:
                return "Fiddle";
            case 112:
                return "Shanai";
            case 113:
                return "Tinkle Bell";
            case 114:
                return "Agogo";
            case 115:
                return "Steel Drums";
            case 116:
                return "Woodblock";
            case 117:
                return "Taiko Drum";
            case 118:
                return "Melodic Tom";
            case 119:
                return "Synth Drum";
            case 120:
                return "Reverse Cymbal";
            case 121:
                return "Guitar Fret Noise";
            case 122:
                return "Breath Noise";
            case 123:
                return "Seashore";
            case 124:
                return "Bird Tweet";
            case 125:
                return "Telephone Ring";
            case 126:
                return "Helicopter";
            case 127:
                return "Applause";
            case 128:
                return "Gunshot";
            default:
                return "";
        }
    }
}
