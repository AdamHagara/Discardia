package sk.adamhagara.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class WinScreen extends ScreenAdapter {
    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private GlyphLayout glyphLayout;

    private float timer = 5f;

    public WinScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        try {
            this.font = new BitmapFont(Gdx.files.internal("Horror-font.ttf"));
        } catch (Exception e) {
            this.font = new BitmapFont(); // fallback to default font
        }
        this.font.getData().setScale(4);
        this.glyphLayout = new GlyphLayout();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1920, 1080);
        this.camera.position.set(960, 540, 0);
        this.camera.update();
    }

    @Override
    public void show() {
        game.applyCursor();
        game.centerCursor();
    }

    @Override
    public void render(float delta) {
        game.applyCursor();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        timer -= delta;
        if (timer <= 0) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        ScreenUtils.clear(Color.BLACK);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.setColor(Color.GREEN);

        // Center the text
        String text = "You Won!";
        glyphLayout.setText(font, text);
        float x = 960 - glyphLayout.width / 2f;
        float y = 540 + glyphLayout.height / 2f;
        font.draw(batch, glyphLayout, x, y);

        font.setColor(Color.WHITE);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

