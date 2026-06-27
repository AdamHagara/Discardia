package sk.adamhagara.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import sk.adamhagara.game.effects.FlyingCard;

public class MenuScreen extends ScreenAdapter {
    private final Main game;
    private SpriteBatch batch;
    private float totalTime = 0;
    private float[] lastUsedTime;
    private Music ambientMusic;

    private final float TITLE_FADE_SPEED = 0.5f;
    private final float BUTTONS_START_TIME = 2.0f;
    private final float BUTTONS_FADE_SPEED = 1.0f;
    private final float PLAY_BUTTON_Y_RATIO = 0.48f;
    private final float EXIT_BUTTON_Y_RATIO = 0.36f;
    private final float BUTTON_TOUCH_PADDING_X = 30f;
    private final float BUTTON_TOUCH_PADDING_Y = 20f;

    private Array<Texture> cardTextures;
    private Array<FlyingCard> cards;

    private BitmapFont font;
    private BitmapFont titleFont;
    private final GlyphLayout layout;
    private final Rectangle playBounds = new Rectangle();
    private final Rectangle exitBounds = new Rectangle();

    public MenuScreen(Main game) {
        this.game = game;
        this.layout = new GlyphLayout();
    }

    @Override
    public void show() {
        game.applyCursor();
        batch = new SpriteBatch();
        cardTextures = new Array<>();

        String[] cardNames = {"FEAR.png", "ANGER.png", "GUILT.png", "DEPRESSION.png",
            "ANXIETY.png", "SHAME.png", "DOUBT.png", "LONELINESS.png"};

        lastUsedTime = new float[cardNames.length];
        for (int i = 0; i < cardNames.length; i++) {
            cardTextures.add(new Texture(Gdx.files.internal(cardNames[i])));
            lastUsedTime[i] = -10.0f;
        }

        cards = new Array<>();
        int maxCards = 5;
        float screenWidth = Gdx.graphics.getWidth();

        for (int i = 0; i < maxCards; i++) {
            FlyingCard c = new FlyingCard();
            c.textureIndex = MathUtils.random(0, cardTextures.size - 1);
            c.x = MathUtils.random(-screenWidth * 0.22f, screenWidth * 0.78f);
            c.y = -800f - (i * 400f);
            cards.add(c);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Horror-font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();

        parameter.size = 105;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        parameter.size = 300;
        parameter.color = Color.RED;
        titleFont = generator.generateFont(parameter);

        generator.dispose();

        // Load and play ambient music
        try {
            ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("Ambient.mp3"));
            ambientMusic.setLooping(true);
            ambientMusic.setVolume(0.5f);
            ambientMusic.play();
        } catch (Exception e) {
            ambientMusic = null;
        }
    }

    @Override
    public void resume() {
        game.applyCursor();
        // Resume ambient music
        if (ambientMusic != null && !ambientMusic.isPlaying()) {
            ambientMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        game.applyCursor();
        totalTime += delta;
        ScreenUtils.clear(Color.BLACK);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float centerX = screenWidth / 2f;

        batch.begin();

        if (totalTime > BUTTONS_START_TIME) {
            float buttonsAlpha = MathUtils.clamp((totalTime - BUTTONS_START_TIME) * BUTTONS_FADE_SPEED, 0, 1);
            batch.setColor(1, 1, 1, buttonsAlpha);

            for (FlyingCard card : cards) {
                if (card.y > screenHeight + 400) {
                    card.reset();
                    card.x = MathUtils.random(-screenWidth * 0.22f, screenWidth * 0.78f);

                    int newIndex;
                    int attempts = 0;
                    do {
                        newIndex = MathUtils.random(0, cardTextures.size - 1);
                        attempts++;
                    } while (totalTime - lastUsedTime[newIndex] < 2.0f && attempts < 20);

                    card.textureIndex = newIndex;
                    lastUsedTime[newIndex] = totalTime;
                }

                card.update(delta);
                Texture tex = cardTextures.get(card.textureIndex);

                float desiredWidth = screenWidth * 0.45f;
                float aspectRatio = (float) tex.getHeight() / tex.getWidth();
                float desiredHeight = desiredWidth * aspectRatio;

                batch.draw(tex, card.x, card.y, desiredWidth / 2f, desiredHeight / 2f,
                    desiredWidth, desiredHeight, 1f, 1f, card.rotation,
                    0, 0, tex.getWidth(), tex.getHeight(), false, false);
            }
            batch.setColor(Color.WHITE);
        }

        float titleAlpha = MathUtils.clamp(totalTime * TITLE_FADE_SPEED, 0, 1);
        drawText(titleFont, "DISCARDIA", screenHeight * 0.85f, new Color(1, 0, 0, titleAlpha));

        if (totalTime > BUTTONS_START_TIME) {
            float buttonsAlpha = MathUtils.clamp((totalTime - BUTTONS_START_TIME) * BUTTONS_FADE_SPEED, 0, 1);
            drawButton("PLAY", screenHeight * PLAY_BUTTON_Y_RATIO, new Color(1, 1, 1, buttonsAlpha), playBounds);
            drawButton("EXIT", screenHeight * EXIT_BUTTON_Y_RATIO, new Color(1, 0, 0, buttonsAlpha), exitBounds);
        }

        game.drawSoftwareCursor(batch, Gdx.input.getX(), screenHeight - Gdx.input.getY());

        batch.end();

        if (totalTime > BUTTONS_START_TIME + 0.5f) {
            handleInput(centerX, screenHeight);
        }
    }

    private void drawText(BitmapFont targetFont, String text, float y, Color color) {
        targetFont.setColor(color);
        layout.setText(targetFont, text);
        targetFont.draw(batch, text, (Gdx.graphics.getWidth() - layout.width) / 2f, y);
    }

    private void drawButton(String text, float y, Color color, Rectangle outBounds) {
        font.setColor(color);
        layout.setText(font, text);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        font.draw(batch, text, x, y);
        outBounds.set(
            x - BUTTON_TOUCH_PADDING_X,
            y - layout.height - BUTTON_TOUCH_PADDING_Y,
            layout.width + BUTTON_TOUCH_PADDING_X * 2f,
            layout.height + BUTTON_TOUCH_PADDING_Y * 2f
        );
    }

    private void handleInput(float centerX, float screenHeight) {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = screenHeight - Gdx.input.getY();

            if (playBounds.contains(touchX, touchY)) {
                game.setScreen(new GameScreen(game));
            } else if (exitBounds.contains(touchX, touchY)) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture t : cardTextures) t.dispose();
        font.dispose();
        titleFont.dispose();
        // Dispose ambient music
        if (ambientMusic != null) {
            ambientMusic.stop();
            ambientMusic.dispose();
        }
    }
}

