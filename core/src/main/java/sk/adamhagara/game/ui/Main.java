package sk.adamhagara.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private Cursor customCursor;
    private Texture softwareCursorTexture;

    private static final int CURSOR_HOTSPOT_X = 0;
    private static final int CURSOR_HOTSPOT_Y = 0;
    private static final float SOFTWARE_CURSOR_SCALE = 0.16f;

    @Override
    public void create() {
        initCursor();
        this.setScreen(new MenuScreen(this));
    }

    public void applyCursor() {
        Gdx.input.setCursorCatched(false);
        if (softwareCursorTexture != null) {
            try {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
            } catch (Exception ignored) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        } else if (customCursor != null) {
            Gdx.graphics.setCursor(customCursor);
        } else {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }
    }

    public void centerCursor() {
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    public void drawSoftwareCursor(SpriteBatch batch, float cursorX, float cursorY) {
        if (softwareCursorTexture == null) return;
        float scaledWidth = softwareCursorTexture.getWidth() * SOFTWARE_CURSOR_SCALE;
        float scaledHeight = softwareCursorTexture.getHeight() * SOFTWARE_CURSOR_SCALE;
        float scaledHotspotX = CURSOR_HOTSPOT_X * SOFTWARE_CURSOR_SCALE;
        float scaledHotspotY = CURSOR_HOTSPOT_Y * SOFTWARE_CURSOR_SCALE;
        float drawX = cursorX - scaledHotspotX;
        float drawY = cursorY - (scaledHeight - scaledHotspotY);
        batch.setColor(Color.WHITE);
        batch.draw(softwareCursorTexture, drawX, drawY, scaledWidth, scaledHeight);
    }

    private void initCursor() {
        try {
            Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("Cursor.png"));
            customCursor = Gdx.graphics.newCursor(cursorPixmap, CURSOR_HOTSPOT_X, CURSOR_HOTSPOT_Y);
            cursorPixmap.dispose();
        } catch (Exception ignored) {
            customCursor = null;
        }

        try {
            softwareCursorTexture = new Texture(Gdx.files.internal("Cursor.png"));
        } catch (Exception ignored) {
            softwareCursorTexture = null;
        }

        applyCursor();
        centerCursor();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (customCursor != null) {
            customCursor.dispose();
        }
        if (softwareCursorTexture != null) {
            softwareCursorTexture.dispose();
        }
    }
}

