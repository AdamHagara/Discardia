package sk.adamhagara.game.cards.special;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.managers.SoundManager;

public class CurseCard extends SpecialCard {
    private final Entity entity;
    private Sound laughSound;

    public CurseCard(Entity entity) {
        super("Curse", "S_CURSE.png");
        this.entity = entity;
        try {
            this.laughSound = Gdx.audio.newSound(Gdx.files.internal("Laugh.mp3"));
            SoundManager.registerSound(laughSound);
        } catch (Exception ignored) {
            this.laughSound = null;
        }
    }

    @Override
    public void applyEffect() {
        entity.curse(8f, 0f);
        if (laughSound != null) laughSound.play(1.0f);
    }
}

