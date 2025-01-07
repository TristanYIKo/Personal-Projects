package hellowrold;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffectPlayer {
    private float volume = 0.9f; // Volume range from 0.0 (mute) to 1.0 (full volume)
    private Clip currentClip;

    public void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);

            // Check if the file exists
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFilePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set the volume
            setClipVolume(clip, volume);

            clip.start();
            currentClip = clip;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file: " + soundFilePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error while playing sound file: " + soundFilePath);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable for playing sound file: " + soundFilePath);
            e.printStackTrace();
        }
    }

    private void setClipVolume(Clip clip, float volume) {
        if (volume < 0.0f || volume > 1.0f) {
            throw new IllegalArgumentException("Volume not valid: " + volume);
        }
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float range = max - min;
        float gain = (range * volume) + min;
        volumeControl.setValue(gain);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (currentClip != null && currentClip.isOpen()) {
            setClipVolume(currentClip, volume);
        }
    }
}
