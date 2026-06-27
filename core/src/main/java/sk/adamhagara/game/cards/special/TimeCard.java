package sk.adamhagara.game.cards.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.StopWatch;
import sk.adamhagara.game.managers.SoundManager;

public class TimeCard extends SpecialCard {
    private final StopWatch stopwatch;
    private Sound timeCardSound;

    public TimeCard(StopWatch stopwatch) {
        super("Time", "S_TIME.png");
        this.stopwatch = stopwatch;
        try {
            this.timeCardSound = Gdx.audio.newSound(Gdx.files.internal("Time_Card.mp3"));
            SoundManager.registerSound(timeCardSound);
        } catch (Exception ignored) {
            this.timeCardSound = null;
        }
    }

    @Override
    public void applyEffect() {
        stopwatch.addTime(15f);
        if (timeCardSound != null) timeCardSound.play(1.0f);
    }
}

