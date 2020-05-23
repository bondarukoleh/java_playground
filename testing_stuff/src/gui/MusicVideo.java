package gui;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class MusicVideo {
    public static void main(String[] args) {
        MusicVideoPlayer musicVideoPlayer = new MusicVideoPlayer();
        musicVideoPlayer.go();
    }
}

class MusicVideoPlayer {
    static JFrame videoFrame = new JFrame("My First Music Video");
    static MyDrawPanel myDrawPanel;

    public void go() {
        int[] eventsToListen = {127};
        this.setUpGui();
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.addControllerEventListener(myDrawPanel, eventsToListen);
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();
            int randomNum = 0;
            for (int i = 0; i < 60; i += 4) {
                randomNum = (int) ((Math.random() * 50) + 1);
                track.add(EventMaker.makeEvent(144, 1, randomNum, 100, i));
                track.add(EventMaker.makeEvent(176, 1, 127, 0, i));
                track.add(EventMaker.makeEvent(128, 1, randomNum, 100, i + 2));
            }
            sequencer.setSequence(sequence);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUpGui() {
        myDrawPanel = new MyDrawPanel();
        videoFrame.setContentPane(myDrawPanel);
        videoFrame.setBounds(30, 30, 800, 800);
        videoFrame.setVisible(true);
    }
}

class MyDrawPanel extends JPanel implements ControllerEventListener {
    boolean msg = false;

    public void controlChange(ShortMessage event) {
        msg = true;
        repaint();
    }

    public void paintComponent(Graphics g) {
        if (msg) {
            Graphics2D g2 = (Graphics2D) g;
            int r = (int) (Math.random() * 250);
            int gr = (int) (Math.random() * 250);
            int b = (int) (Math.random() * 250);
            g2.setColor(new Color(r, gr, b));
            int ht = (int) ((Math.random() * 120) + 10);
            int width = (int) ((Math.random() * 120) + 10);
            int x = (int) ((Math.random() * 40) + 10);
            int y = (int) ((Math.random() * 40) + 10);
            g2.fillRect(x, y, width, ht);
            msg = false;
        }
    }
}