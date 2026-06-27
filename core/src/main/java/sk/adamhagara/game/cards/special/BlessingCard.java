package sk.adamhagara.game.cards.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.patterns.visitor.CardVisitor;

public class BlessingCard extends SpecialCard {
    private final Candle candle;
    private final Entity entity;
    private final Sound blessingSound;

    public BlessingCard(Candle candle, Entity entity) {
        super("Blessing", "S_BLESSING.png");
        this.candle = candle;
        this.entity = entity;
        this.blessingSound = Gdx.audio.newSound(Gdx.files.internal("Blessing_audio.mp3"));
        SoundManager.registerSound(blessingSound);
    }

    @Override
    public void applyEffect() {
        candle.applyBlessing(25f);
        entity.pacify(10f);
        if (blessingSound != null) blessingSound.play(1.0f);
    }

    // Visitor pattern support
    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }

    // Getters for validation
    public Candle getCandle() { return candle; }
    public Entity getEntity() { return entity; }
}

