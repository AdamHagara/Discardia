package sk.adamhagara.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.adamhagara.game.effects.Effect;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.patterns.observer.GameEventManager;

public class Candle extends GameObject implements Effect {
    private static final Logger logger = LoggerFactory.getLogger(Candle.class);
    private boolean isLit = true;
    private float fuel = 100f;
    private float burnSpeed = 2.5f;
    private float blessingTimer = 0;
    private float fakeExtinguishTimer = 0;
    private float anxietyBurnTimer = 0;
    private static final float ANXIETY_BURN_MULTIPLIER = 2.0f;

    private final Texture litDownlookTexture;
    private final Texture unlitDownlookTexture;
    private final Sound blowCandleSound;
    private final Sound matchLightingSound;

    public Candle(float x, float y) {
        super(x, y);
        if (Gdx.files != null) {
            litDownlookTexture = new Texture(Gdx.files.internal("Candle.png"));
            unlitDownlookTexture = new Texture(Gdx.files.internal("unlit_Candle.png"));
            blowCandleSound = Gdx.audio.newSound(Gdx.files.internal("Blow_candle.mp3"));
            matchLightingSound = Gdx.audio.newSound(Gdx.files.internal("Match_lighting.mp3"));

            // Register sounds with SoundManager
            SoundManager.registerSound(blowCandleSound);
            SoundManager.registerSound(matchLightingSound);
        } else {
            litDownlookTexture = null;
            unlitDownlookTexture = null;
            blowCandleSound = null;
            matchLightingSound = null;
        }
    }

    public void relight() {
        logger.info("Candle relit - fuel restored to 100%");
        this.isLit = true;
        this.fuel = 100f;
        if (matchLightingSound != null) matchLightingSound.play(1.0f);

        // Observer pattern notification
        GameEventManager.getInstance().notifyCandleRelit();
    }

    public void relight(float bonus) {
        this.isLit = true;
        this.fuel = Math.min(120f, this.fuel + bonus);
        if (matchLightingSound != null) matchLightingSound.play(1.0f);
    }

    public void applyBlessing(float duration) {
        this.blessingTimer = duration;
        this.relight(20f);
    }

    public void extinguish() {
        if (blessingTimer > 0) {
            logger.debug("Candle extinguish blocked by blessing protection");
            return;
        }
        logger.warn("Candle extinguished - fuel remaining: {}%", getFuelPercent());
        this.isLit = false;
        if (blowCandleSound != null) blowCandleSound.play(1.0f);

        // Observer pattern notification
        GameEventManager.getInstance().notifyCandleExtinguished();
    }

    public void forceExtinguish() {
        logger.warn("Candle force extinguished - fuel remaining: {}%", getFuelPercent());
        this.isLit = false;
        if (blowCandleSound != null) blowCandleSound.play(1.0f);

        // Observer pattern notification
        GameEventManager.getInstance().notifyCandleExtinguished();
    }

    public void fakeExtinguish(float duration) {
        this.fakeExtinguishTimer = duration;
    }

    public void applyAnxiety(float duration) {
        this.anxietyBurnTimer = duration;
    }

    @Override
    public void update(float delta) {
        if (blessingTimer > 0) blessingTimer -= delta;
        if (fakeExtinguishTimer > 0) {
            fakeExtinguishTimer -= delta;
        }
        if (isLit) {
            // Apply 2x burn speed if anxiety is active
            float effectiveBurnSpeed = burnSpeed;
            if (anxietyBurnTimer > 0) {
                effectiveBurnSpeed = burnSpeed * ANXIETY_BURN_MULTIPLIER;
            }

            fuel -= delta * effectiveBurnSpeed;
            if (fuel <= 0) {
                fuel = 0;
                // Cancel anxiety effect when candle burns out
                this.anxietyBurnTimer = 0;
                this.burnSpeed = 2.5f;  // Reset to normal burn speed
                if (blessingTimer <= 0) applyEffect();
            } else if (fuel <= 10f && fuel > 9.9f) {
                // Observer pattern notification - critical fuel
                GameEventManager.getInstance().notifyCandleFuelCritical(fuel);
            } else if (fuel <= 25f && fuel > 24.9f) {
                // Observer pattern notification - low fuel
                GameEventManager.getInstance().notifyCandleFuelLow(fuel);
            }
        }
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        boolean effectivelyLit = isLit && fakeExtinguishTimer <= 0;
        Texture currentTexture = effectivelyLit ? litDownlookTexture : unlitDownlookTexture;

        // Use existing batch color instead of forcing white
        float currentWidth = currentTexture.getWidth();
        float currentHeight = currentTexture.getHeight();
        float currentDrawX = x - currentWidth / 2f;
        float currentDrawY = y - currentHeight / 2f;
        batch.draw(currentTexture, currentDrawX, currentDrawY, currentWidth, currentHeight);

        float shadowHeight = currentHeight * 0.45f;
        // Apply shadow color on top of existing batch color (for FearCard effect)
        Color currentColor = batch.getColor();
        batch.setColor(currentColor.r * 0.45f, currentColor.g * 0.40f, currentColor.b * 0.35f, currentColor.a * 0.75f);
        batch.draw(currentTexture, currentDrawX, currentDrawY, currentWidth, shadowHeight,
            0, (int) (currentTexture.getHeight() * 0.55f),
            currentTexture.getWidth(), (int) (currentTexture.getHeight() * 0.45f),
            false, false);
        batch.setColor(currentColor); // Restore original color instead of forcing white
    }

    public void fillBounds(Rectangle bounds) {
        boolean effectivelyLit = isLit && fakeExtinguishTimer <= 0;
        Texture currentTexture = effectivelyLit ? litDownlookTexture : unlitDownlookTexture;
        float currentWidth = currentTexture.getWidth();
        float currentHeight = currentTexture.getHeight();
        bounds.set(x - currentWidth / 2f, y - currentHeight / 2f, currentWidth, currentHeight);
    }

    public void dispose() {
        litDownlookTexture.dispose();
        unlitDownlookTexture.dispose();
        if (blowCandleSound != null) blowCandleSound.dispose();
        if (matchLightingSound != null) matchLightingSound.dispose();
    }


    @Override
    public void applyEffect() {
        this.isLit = false;
        if (blowCandleSound != null) blowCandleSound.play(1.0f);
    }

    public boolean isLit() { return isLit; }
    public int getFuelPercent() { return (int) fuel; }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Candle[x=%.1f, y=%.1f, lit=%s, fuel=%d%%]",
                           x, y, isLit, getFuelPercent());
    }
}
