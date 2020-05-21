package src.player;

import javax.sound.midi.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class RunPlayer2 {
    public static void main(String[] args) {
        PlayerCMD mini = new PlayerCMD();
        IOHelper2 ioHelper2 = new IOHelper2();
        System.out.println("Please enter two numbers from 0 to 127, space separated. First represent instrument, second" +
                "is note to play");
        while (true) {
            int[] userHit = ioHelper2.getUserInput();
            if (userHit.length < 2) {
                System.out.println("Don’t forget the instrument and note args");
            } else {
                int instrument = userHit[0];
                int note = userHit[1];
                mini.play(instrument, note);
            }
        }
    }
}

public class PlayerCMD {
    public void play(int instrument, int note) {
        try {
            Sequencer player = MidiSystem.getSequencer();
            player.open();
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();
            MidiEvent event = null;
            ShortMessage first = new ShortMessage();
            first.setMessage(192, 1, instrument, 0);
            MidiEvent changeInstrument = new MidiEvent(first, 1);
            track.add(changeInstrument);
            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, note, 100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);
            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, note, 100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            track.add(noteOff);
            player.setSequence(seq);
            player.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // close play
} // close class

class IOHelper2 {
    public int[] getUserInput() {
        String userEnter = null;
        int[] parsedNumbers = new int[2];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            userEnter = reader.readLine();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        if (userEnter instanceof String) {
            String[] numbers = userEnter.split(" ");
            if (numbers.length == 2) {
                parsedNumbers[0] = Integer.parseInt(numbers[0]);
                parsedNumbers[1] = Integer.parseInt(numbers[1]);
            }
        }
        return parsedNumbers;
    }
}