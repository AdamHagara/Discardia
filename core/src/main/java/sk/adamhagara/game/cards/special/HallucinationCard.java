package sk.adamhagara.game.cards.special;

import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.ui.GameScreen;

public class HallucinationCard extends SpecialCard {
    private final Candle candle;
    private final GameScreen gameScreen;

    public HallucinationCard(Candle candle, GameScreen gameScreen) {
        super("Hallucination", "S_HALLUCINATION.png");
        this.candle = candle;
        this.gameScreen = gameScreen;
    }

    @Override
    public void applyEffect() {
        candle.fakeExtinguish(2.5f);
        gameScreen.startHallucination(2.5f, 8f);
    }
}

