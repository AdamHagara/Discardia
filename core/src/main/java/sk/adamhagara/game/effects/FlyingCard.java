package sk.adamhagara.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class FlyingCard {
    public float x, y, speed, rotation, rotSpeed;
    public int textureIndex;

    public FlyingCard() {
        // Inicializacia
        reset();
    }

    public void reset() {
        // X pozicia v menuscreen
        // Vertikalny start a pohyb
        y = MathUtils.random(-800, -600);

        // Rychlost
        speed = MathUtils.random(150, 250);
        // Rotacia
        rotSpeed = MathUtils.random(-0.2f, 0.2f);
        rotation = MathUtils.random(0, 360);
    }

    public void update(float delta) {
        y += speed * delta;
        rotation += rotSpeed;

        // Ak vyleti vysoko tak sa vrati dole
        if (y > Gdx.graphics.getHeight() + 500) {
            reset();
        }
    }
}

