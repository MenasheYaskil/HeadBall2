import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer implements AutoCloseable {
    private Clip clip;

    public AudioPlayer(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("The audio file does not exist: " + filePath);
                return;
            }
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // start from beginning
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    @Override
    public void close() {
        if (clip != null) {
            try {
                clip.stop();
            } finally {
                clip.close();
            }
        }
    }
}
