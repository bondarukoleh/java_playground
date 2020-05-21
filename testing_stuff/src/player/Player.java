package src.player;

import javax.sound.midi.*;

public class Player {
    public void play() throws MidiUnavailableException {
        try {
            Sequencer player = MidiSystem.getSequencer();
            player.open();
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();

            ShortMessage shortMessage = new ShortMessage();
            shortMessage.setMessage(144, 1, 102, 100);
            MidiEvent noteOn = new MidiEvent(shortMessage, 1);
            track.add(noteOn);

            ShortMessage shortMessage2 = new ShortMessage();
            shortMessage2.setMessage(128, 1, 102, 100);
            MidiEvent noteOff = new MidiEvent(shortMessage2, 3);
            track.add(noteOff);

            player.setSequence(sequence);
            player.start();

        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            System.out.println("Player error occurred");
            e.printStackTrace();
        }
    }
}
