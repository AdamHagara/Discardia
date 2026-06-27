package sk.adamhagara.game.managers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * Global sound manager to track and control all active game sounds
 */
public class SoundManager {
    private static final Array<Sound> activeSounds = new Array<>();

    /**
     * Register a sound to be tracked
     */
    public static void registerSound(Sound sound) {
        if (sound != null && !activeSounds.contains(sound, true)) {
            activeSounds.add(sound);
        }
    }

    /**
     * Stop all currently playing sounds
     */
    public static void stopAllSounds() {
        for (Sound sound : activeSounds) {
            if (sound != null) {
                try {
                    sound.stop();
                } catch (Exception ignored) {
                    // Sound might be disposed, ignore
                }
            }
        }
    }
}

