package src.player;

import javax.sound.midi.MidiUnavailableException;

public class RunPlayer {
    public static void main(String[] args) throws MidiUnavailableException {
        Player player = new Player();
        player.play();
    }
}
