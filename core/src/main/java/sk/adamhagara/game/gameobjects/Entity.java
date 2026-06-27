package sk.adamhagara.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.patterns.observer.GameEventManager;

public class Entity extends GameObject {
    private static final float ENTITY_SCALE = 2.2f;
    private static final float ENTITY_TINT = 0.72f;
    private static final float MAX_ANGER_LEVEL = 100f;
    private static final float MIN_ANGER_LEVEL = 0f;

    private float angerLevel = 0;
    private final BitmapFont font;
    private float pacifyTimer = 0;

    private float curseTimer = 0;
    private boolean curseApplied = false;

    private final EffectTimers effectTimers;

    private final Textures textures;
    private final Sounds sounds;
    private final SoundFlags soundFlags;

    private boolean gameOver = false;

    public Entity(float x, float y, BitmapFont font) {
        super(x, y);
        this.font = font;
        this.textures = new Textures();
        this.sounds = new Sounds();
        this.soundFlags = new SoundFlags();
        this.effectTimers = new EffectTimers();
    }

    /**
     * Inner class for managing texture resources
     */
    private class Textures {
        private final Texture entityTexture;
        private final Texture entityAngeredTexture;
        private final Texture entityJumpscareTexture;
        private final float renderWidth;
        private final float renderHeight;

        public Textures() {
            if (Gdx.files != null) {
                entityTexture = new Texture(Gdx.files.internal("Entity.png"));
                entityAngeredTexture = new Texture(Gdx.files.internal("Entity_angered.png"));
                entityJumpscareTexture = new Texture(Gdx.files.internal("Entity_jumpscare.png"));

                // Keep pixels sharp when scaling the sprite up.
                entityTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                entityAngeredTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                entityJumpscareTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

                renderWidth = entityTexture.getWidth() * ENTITY_SCALE;
                renderHeight = entityTexture.getHeight() * ENTITY_SCALE;
            } else {
                entityTexture = null;
                entityAngeredTexture = null;
                entityJumpscareTexture = null;
                renderWidth = 0f;
                renderHeight = 0f;
            }
        }

        public Texture getEntityTexture() { return entityTexture; }
        public Texture getEntityAngeredTexture() { return entityAngeredTexture; }
        public Texture getEntityJumpscareTexture() { return entityJumpscareTexture; }
        public float getRenderWidth() { return renderWidth; }
        public float getRenderHeight() { return renderHeight; }

        public void dispose() {
            if (entityTexture != null) entityTexture.dispose();
            if (entityAngeredTexture != null) entityAngeredTexture.dispose();
            if (entityJumpscareTexture != null) entityJumpscareTexture.dispose();
        }
    }

    /**
     * Inner class for managing sound resources
     */
    private class Sounds {
        private final Sound chairMovingSound;
        private final Sound tableMovingSound;

        public Sounds() {
            if (Gdx.files != null) {
                chairMovingSound = Gdx.audio.newSound(Gdx.files.internal("Chair_moving.mp3"));
                tableMovingSound = Gdx.audio.newSound(Gdx.files.internal("Table_moving.mp3"));

                // Register sounds with SoundManager
                SoundManager.registerSound(chairMovingSound);
                SoundManager.registerSound(tableMovingSound);
            } else {
                chairMovingSound = null;
                tableMovingSound = null;
            }
        }

        public Sound getChairMovingSound() { return chairMovingSound; }
        public Sound getTableMovingSound() { return tableMovingSound; }

        public void dispose() {
            if (chairMovingSound != null) chairMovingSound.dispose();
            if (tableMovingSound != null) tableMovingSound.dispose();
        }
    }

    /**
     * Inner class for managing sound flags
     */
    private class SoundFlags {
        private boolean chairMovingSoundPlayed = false;
        private boolean tableMovingSoundPlayed = false;

        public boolean isChairMovingSoundPlayed() { return chairMovingSoundPlayed; }
        public boolean isTableMovingSoundPlayed() { return tableMovingSoundPlayed; }

        public void setChairMovingSoundPlayed(boolean played) { chairMovingSoundPlayed = played; }
        public void setTableMovingSoundPlayed(boolean played) { tableMovingSoundPlayed = played; }

        public void reset() {
            chairMovingSoundPlayed = false;
            tableMovingSoundPlayed = false;
        }
    }

    /**
     * Inner class for managing effect timers
     */
    private class EffectTimers {
        private float angerEffectTimer = 0f;
        private float anxietyEffectTimer = 0f;
        private float depressionEffectTimer = 0f;
        private float doubtEffectTimer = 0f;
        private float fearEffectTimer = 0f;
        private float guiltEffectTimer = 0f;
        private float lonelinessEffectTimer = 0f;
        private float shameEffectTimer = 0f;

        public void updateTimers(float delta) {
            if (angerEffectTimer > 0) angerEffectTimer -= delta;
            if (anxietyEffectTimer > 0) anxietyEffectTimer -= delta;
            if (depressionEffectTimer > 0) depressionEffectTimer -= delta;
            if (doubtEffectTimer > 0) doubtEffectTimer -= delta;
            if (fearEffectTimer > 0) fearEffectTimer -= delta;
            if (guiltEffectTimer > 0) guiltEffectTimer -= delta;
            if (lonelinessEffectTimer > 0) lonelinessEffectTimer -= delta;
            if (shameEffectTimer > 0) shameEffectTimer -= delta;
        }

        // Getters and setters for all timers
        public float getAngerEffectTimer() { return angerEffectTimer; }
        public float getAnxietyEffectTimer() { return anxietyEffectTimer; }
        public float getDepressionEffectTimer() { return depressionEffectTimer; }
        public float getDoubtEffectTimer() { return doubtEffectTimer; }
        public float getFearEffectTimer() { return fearEffectTimer; }
        public float getGuiltEffectTimer() { return guiltEffectTimer; }
        public float getLonelinessEffectTimer() { return lonelinessEffectTimer; }
        public float getShameEffectTimer() { return shameEffectTimer; }

        public void setAngerEffectTimer(float timer) { angerEffectTimer = timer; }
        public void setAnxietyEffectTimer(float timer) { anxietyEffectTimer = timer; }
        public void setDepressionEffectTimer(float timer) { depressionEffectTimer = timer; }
        public void setDoubtEffectTimer(float timer) { doubtEffectTimer = timer; }
        public void setFearEffectTimer(float timer) { fearEffectTimer = timer; }
        public void setGuiltEffectTimer(float timer) { guiltEffectTimer = timer; }
        public void setLonelinessEffectTimer(float timer) { lonelinessEffectTimer = timer; }
        public void setShameEffectTimer(float timer) { shameEffectTimer = timer; }
    }

    // Public getters/setters with proper encapsulation
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public float getAngerLevel() { return angerLevel; }
    public void setAngerLevel(float angerLevel) {
        float oldLevel = this.angerLevel;
        this.angerLevel = Math.max(MIN_ANGER_LEVEL, Math.min(MAX_ANGER_LEVEL, angerLevel));
        
        // Observer pattern notifications
        GameEventManager.getInstance().notifyEntityAngerChanged(this.angerLevel);
        
        if (this.angerLevel >= 80f && oldLevel < 80f) {
            GameEventManager.getInstance().notifyEntityAngerCritical(this.angerLevel);
        } else if (this.angerLevel >= 60f && oldLevel < 60f) {
            GameEventManager.getInstance().notifyEntityAngerHigh(this.angerLevel);
        }
    }

    public boolean isSpriteVisible() { return isVisible(); }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    public void renderBehindTable(SpriteBatch batch) {
        if (!isVisible() || textures.getEntityTexture() == null) return;

        Texture textureToRender = selectTexture();
        float renderX = getX() - textures.getRenderWidth() / 2f;
        float renderY = getY() - textures.getRenderHeight() / 2f;

        renderWithTint(batch, textureToRender, renderX, renderY);
    }

    public void renderBehindTableWithAngeredY(SpriteBatch batch, float normalY, float angeredY) {
        if (!isVisible() || textures.getEntityTexture() == null) return;

        Texture textureToRender = selectTexture();
        float renderX = getX() - textures.getRenderWidth() / 2f;
        float renderY = selectRenderY(normalY, angeredY);

        renderWithTint(batch, textureToRender, renderX, renderY);
    }

    /**
     * Select appropriate texture based on game state
     */
    private Texture selectTexture() {
        if (gameOver && textures.getEntityJumpscareTexture() != null) {
            return textures.getEntityJumpscareTexture();
        } else if (angerLevel >= 60 && textures.getEntityAngeredTexture() != null && !gameOver) {
            return textures.getEntityAngeredTexture();
        }
        return textures.getEntityTexture();
    }

    /**
     * Select Y position based on game state
     */
    private float selectRenderY(float normalY, float angeredY) {
        if (gameOver) {
            return normalY; // Jumpscare uses normal Y
        } else if (angerLevel >= 60 && !gameOver) {
            return angeredY; // Angered uses custom Y
        }
        return normalY;
    }

    /**
     * Render texture with entity tint
     */
    private void renderWithTint(SpriteBatch batch, Texture texture, float x, float y) {
        Color currentColor = batch.getColor();
        batch.setColor(currentColor.r * ENTITY_TINT, currentColor.g * ENTITY_TINT, currentColor.b * ENTITY_TINT, currentColor.a);
        batch.draw(texture, x, y, textures.getRenderWidth(), textures.getRenderHeight());
        batch.setColor(currentColor); // Restore original color
    }

    public void renderOverlay(SpriteBatch batch) {
        drawStatusText(batch);
    }

    // Effect trigger methods with proper encapsulation
    public void pacify(float duration) {
        this.pacifyTimer = Math.max(0, duration);
    }

    public void curse(float duration, float angerSpike) {
        this.curseTimer = Math.max(0, duration);
        this.curseApplied = false;
        setAngerLevel(angerLevel + angerSpike);
    }

    public void triggerAngerEffect(float duration) {
        effectTimers.setAngerEffectTimer(Math.max(0, duration));
    }


    public void triggerDepressionEffect(float duration) {
        effectTimers.setDepressionEffectTimer(Math.max(0, duration));
    }

    public void triggerDoubtEffect(float duration) {
        effectTimers.setDoubtEffectTimer(Math.max(0, duration));
    }

    public void triggerFearEffect(float duration) {
        effectTimers.setFearEffectTimer(Math.max(0, duration));
    }

    public void triggerGuiltEffect(float duration) {
        effectTimers.setGuiltEffectTimer(Math.max(0, duration));
    }

    public void triggerLonelinessEffect(float duration) {
        effectTimers.setLonelinessEffectTimer(Math.max(0, duration));
    }

    public void triggerShameEffect(float duration) {
        effectTimers.setShameEffectTimer(Math.max(0, duration));
    }

    public void updateLogic(float extraAnger) {
        if (pacifyTimer > 0) return;

        setAngerLevel(angerLevel + extraAnger);
    }

    public void updateLogic(float delta, boolean isLooking) {
        if (pacifyTimer > 0) {
            pacifyTimer -= delta;
            // During blessing/pacify: anger decreases, cannot increase
            setAngerLevel(angerLevel - delta * 10f);
            soundFlags.reset();
            return;
        }

        updateAngerLevel(delta, isLooking);
        updateSoundEffects();
        updateTimers(delta);
    }

    /**
     * Update anger level based on player looking state
     */
    private void updateAngerLevel(float delta, boolean isLooking) {
        if (isLooking) {
            setAngerLevel(angerLevel - delta * 10f);
        } else {
            setAngerLevel(angerLevel + delta * 15f);
        }
    }

    /**
     * Update sound effects based on anger level
     */
    private void updateSoundEffects() {
        if (angerLevel >= 85 && !soundFlags.isTableMovingSoundPlayed()) {
            playSound(sounds.getTableMovingSound(), 1.0f);
            soundFlags.setTableMovingSoundPlayed(true);
            soundFlags.setChairMovingSoundPlayed(true);  // Skip chair sound if table sound plays
        } else if (angerLevel >= 60 && angerLevel < 85 && !soundFlags.isChairMovingSoundPlayed()) {
            playSound(sounds.getChairMovingSound(), 1.0f);
            soundFlags.setChairMovingSoundPlayed(true);
        } else if (angerLevel < 60) {
            soundFlags.reset();
        }
    }

    /**
     * Play sound if available
     */
    private void playSound(Sound sound, float volume) {
        if (sound != null) {
            sound.play(volume);
        }
    }

    /**
     * Update all timers
     */
    private void updateTimers(float delta) {
        if (curseTimer > 0) {
            curseTimer -= delta;
            curseApplied = true;
        }

        // Store previous doubt timer state to detect when it expires
        float previousDoubtTimer = effectTimers.getDoubtEffectTimer();
        effectTimers.updateTimers(delta);
        
        // Restore visibility when doubt effect timer expires
        if (previousDoubtTimer > 0 && effectTimers.getDoubtEffectTimer() <= 0) {
            setVisible(true);
        }
    }

    @Override
    public void onDispose() {
        textures.dispose();
        sounds.dispose();
    }

    @Override
    public void update(float delta) {
        // Default implementation - can be overridden
    }

    private void drawStatusText(SpriteBatch batch) {
        // No text display - can be overridden in subclasses
    }

    @Override
    public String toString() {
        return String.format("Entity[x=%.1f, y=%.1f, anger=%.1f, visible=%s]", 
                           x, y, angerLevel, isVisible());
    }

}

