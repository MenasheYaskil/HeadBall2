import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
    private Clip clip;

    public AudioPlayer(String path) {
        try (AudioInputStream src = openStream(path);
             AudioInputStream converted = toPcm16(src)) {
            DataLine.Info info = new DataLine.Info(Clip.class, converted.getFormat());
            Clip c = (Clip) AudioSystem.getLine(info);
            c.open(converted);
            clip = c;
        } catch (Exception e) {
            System.err.println("AudioPlayer: failed to load '" + path + "': " + e);
            clip = null;
        }
    }

    private static AudioInputStream openStream(String path) throws UnsupportedAudioFileException, IOException {
        String norm = (path.startsWith("/") ? path : "/" + path).replace("\\", "/");
        URL url = AudioPlayer.class.getResource(norm);
        if (url != null) return AudioSystem.getAudioInputStream(url);
        return AudioSystem.getAudioInputStream(new File(path));
    }

    private static AudioInputStream toPcm16(AudioInputStream src) {
        AudioFormat base = src.getFormat();
        float sr = base.getSampleRate() == AudioSystem.NOT_SPECIFIED ? 44100f : base.getSampleRate();
        int ch = base.getChannels() == AudioSystem.NOT_SPECIFIED ? 2 : base.getChannels();
        AudioFormat target = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sr, 16, ch, ch * 2, sr, false);
        return AudioSystem.getAudioInputStream(target, src);
    }

    public void play() {
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void loop(int count) {
        if (clip == null) return;
        clip.loop(count);
    }

    public void pause() {
        if (clip == null) return;
        clip.stop();
    }

    public void stop() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public void close() {
        if (clip == null) return;
        try { clip.stop(); } catch (Exception ignored) {}
        try { clip.flush(); } catch (Exception ignored) {}
        try { clip.close(); } catch (Exception ignored) {}
    }

    public void setVolumeDb(float gainDb) {
        if (clip == null) return;
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(gainDb);
        }
    }

    public void setVolumeLinear(double vol01) {
        if (clip == null) return;
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double v = Math.max(0.0001, Math.min(1.0, vol01));
        float db = (float) (20.0 * Math.log10(v));
        db = Math.max(c.getMinimum(), Math.min(c.getMaximum(), db));
        c.setValue(db);
    }
}
