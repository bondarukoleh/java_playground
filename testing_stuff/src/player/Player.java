package src.player;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class Player {

    public void play() throws MidiUnavailableException {
        Sequencer sequencer = MidiSystem.getSequencer();
        System.out.println("Got sequencer");
    }

    public void play2() {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            System.out.println("Got sequencer");
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }
    }
}
