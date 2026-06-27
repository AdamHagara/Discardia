package sk.adamhagara.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.patterns.observer.GameEventManager;

public class StopWatch extends GameObject {
    private float timeRemaining;
    private BitmapFont font;
    private float lastMinute = 0f;
    private float lastSecond = 0f;
    private Sound clockTickSound;

    public StopWatch(float x, float y, float initialTime, BitmapFont font) {
        super(x, y);
        this.timeRemaining = initialTime;
        this.font = font;
        this.lastMinute = (int)(initialTime / 60f);
        this.lastSecond = (int)initialTime;
        try {
            this.clockTickSound = Gdx.audio.newSound(Gdx.files.internal("Clock_tick.mp3"));
            SoundManager.registerSound(clockTickSound);
        } catch (Exception ignored) {
            this.clockTickSound = null;
        }
    }

    public void addTime(float seconds) {
        timeRemaining += seconds;
    }

    @Override
    public void update(float delta) {
        if (timeRemaining > 0) {
            timeRemaining -= delta;
        } else {
            timeRemaining = 0;
        }

        // Play clock tick sound every minute
        int currentMinute = (int)(timeRemaining / 60f);
        if (currentMinute < lastMinute && timeRemaining > 10) {
            if (clockTickSound != null) clockTickSound.play(1.0f);
            lastMinute = currentMinute;
        }

        // Play clock tick every second for the last 10 seconds
        if (timeRemaining <= 10 && timeRemaining > 0) {
            int currentSecond = (int)timeRemaining;
            if (currentSecond < lastSecond) {
                if (clockTickSound != null) clockTickSound.play(1.0f);
                lastSecond = currentSecond;

                // Observer pattern notifications
                if (currentSecond <= 5 && currentSecond > 4) {
                    GameEventManager.getInstance().notifyTimeCritical(timeRemaining);
                } else if (currentSecond == 10 && lastSecond == 11) {
                    GameEventManager.getInstance().notifyTimeWarning(timeRemaining);
                }
            }
        }
    }


    public float getTimeRemaining() {
        return timeRemaining;
    }
    
    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("StopWatch[x=%.1f, y=%.1f, time=%.1fs]",
                           x, y, timeRemaining);
    }
}
