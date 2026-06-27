package sk.adamhagara.game.cards.special;

import sk.adamhagara.game.gameobjects.Candle;

public class DarknessCard extends SpecialCard {
    private final Candle candle;

    public DarknessCard(Candle candle) {
        super("Darkness", "S_DARKNESS.png");
        this.candle = candle;
    }

    @Override
    public void applyEffect() {
        candle.forceExtinguish();
    }
}

