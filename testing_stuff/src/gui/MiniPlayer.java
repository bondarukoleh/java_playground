package gui;

import javax.sound.midi.*;

public class MiniPlayer {
    public static void main(String[] args) {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();
            for (int i = 5; i < 61; i+= 4) {
                track.add(EventMaker.makeEvent(144,1,i,100,i));
                track.add(EventMaker.makeEvent(128,1,i,100,i + 2));
            } // end loop
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(220);
            sequencer.start();
        } catch (Exception ex) {ex.printStackTrace();}
    }
}

class EventMaker {
    public static MidiEvent makeEvent(int messageType, int channel, int note, int velocity, int timeToThrow) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(messageType, channel, note, velocity);
            event = new MidiEvent(a, timeToThrow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}