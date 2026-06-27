package sk.adamhagara.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import sk.adamhagara.game.managers.SoundManager;

public class JumpscareScreen extends ScreenAdapter {
    private final Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture jumpscareTexture;
    private Sound jumpscareSound;
    private float timer = 5f;

    // Screen shake properties
    private float shakeIntensity = 15f;
    private float shakeDuration = 0.5f;
    private float shakeTimer = 0f;
    private float shakeOffsetX = 0f;
    private float shakeOffsetY = 0f;

    public JumpscareScreen(Main game, String deathReason) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1920, 1080);
        this.camera.position.y = 540;
        this.camera.update();

        // Load fullscreen jumpscare image
        try {
            this.jumpscareTexture = new Texture(Gdx.files.internal("Entity_jumpscare.png"));
        } catch (Exception e) {
            this.jumpscareTexture = null;
        }

        // Load jumpscare sound
        try {
            this.jumpscareSound = Gdx.audio.newSound(Gdx.files.internal("Jumpscare_sound.mp3"));
            SoundManager.registerSound(jumpscareSound);
        } catch (Exception ignored) {
            this.jumpscareSound = null;
        }

        // Stop all sounds when jumpscare appears
        SoundManager.stopAllSounds();
        
        // Initialize screen shake
        this.shakeTimer = shakeDuration;
    }

    @Override
    public void show() {
        game.applyCursor();
        game.centerCursor();
        // Play jumpscare sound
        if (jumpscareSound != null) {
            jumpscareSound.play(1.0f);
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateScreenShake(delta);

        // Countdown timer - return to menu after 5 seconds
        timer -= delta;
        if (timer <= 0) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        ScreenUtils.clear(Color.BLACK);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Render fullscreen jumpscare image
        if (jumpscareTexture != null) {
            batch.draw(jumpscareTexture, 0, 0, 1920, 1080);
        }

        batch.end();
    }

    private void updateScreenShake(float delta) {
        if (shakeTimer > 0) {
            shakeTimer -= delta;

            // Generate random shake offset
            shakeOffsetX = MathUtils.random(-shakeIntensity, shakeIntensity);
            shakeOffsetY = MathUtils.random(-shakeIntensity, shakeIntensity);

            // Apply shake to camera
            camera.position.x = 960 + shakeOffsetX;
            camera.position.y = 540 + shakeOffsetY;
        } else {
            // Reset camera to center when shake ends
            camera.position.x = 960;
            camera.position.y = 540;
        }

        camera.update();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (jumpscareTexture != null) {
            jumpscareTexture.dispose();
        }
        if (jumpscareSound != null) {
            jumpscareSound.dispose();
        }
    }
}

