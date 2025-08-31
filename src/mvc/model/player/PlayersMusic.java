package mvc.model.player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class PlayersMusic {
    private Clip clip;
    private final String player1MusicPath;
    private final String player2MusicPath;

    public PlayersMusic(String player1MusicPath, String player2MusicPath) {
        this.player1MusicPath = player1MusicPath;
        this.player2MusicPath = player2MusicPath;
    }

    public void playMusicForPlayer(int playerNumber) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }

            String musicFile = (playerNumber == 1) ? player1MusicPath : player2MusicPath;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(musicFile));

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing music: " + e.getMessage());
        }
    }

    public void stopMusic() {
        clip.stop();
        clip.close();
    }

}
