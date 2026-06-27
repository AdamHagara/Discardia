package sk.adamhagara.game.cards;

import com.badlogic.gdx.utils.Array;
import sk.adamhagara.game.cards.special.*;
import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.gameobjects.StopWatch;
import sk.adamhagara.game.ui.GameScreen;

public class Deck {
    private Array<Card> cards;

    public Deck(Entity entity, Candle candle, StopWatch stopwatch, GameScreen gameScreen) {
        this.cards = new Array<>();
        fillDeck(entity, candle, stopwatch, gameScreen);
        shuffleDeck();
    }

    private void fillDeck(Entity entity, Candle candle, StopWatch stopwatch, GameScreen gameScreen) {
        // Create emotion cards with varying intensities for polymorphism demonstration
        for (int i = 0; i < 3; i++) {
            cards.add(new AngerCard(entity));
            cards.add(new AnxietyCard(entity, candle));
            cards.add(new DepressionCard(entity, gameScreen));
            cards.add(new DoubtCard(entity));
            cards.add(new FearCard(entity, gameScreen));

            // Create emotion cards with different intensities (compile-time polymorphism)
            float guiltIntensity = 0.8f + (i * 0.2f); // 0.8, 1.0, 1.2
            float lonelinessIntensity = 1.0f + (i * 0.3f); // 1.0, 1.3, 1.6
            float shameIntensity = 0.9f + (i * 0.25f); // 0.9, 1.15, 1.4

            cards.add(new GuiltCard(entity, gameScreen, guiltIntensity));
            cards.add(new LonelinessCard(entity, gameScreen, lonelinessIntensity));
            cards.add(new ShameCard(entity, gameScreen, shameIntensity));
        }

        for (int i = 0; i < 2; i++) {
            cards.add(new BlessingCard(candle, entity));
            cards.add(new CurseCard(entity));
            cards.add(new DarknessCard(candle));
            cards.add(new HallucinationCard(candle, gameScreen));
            cards.add(new TimeCard(stopwatch));
        }
    }

    public void shuffleDeck() {
        cards.shuffle();
    }

    public Card drawCard() {
        if (cards.size > 0) {
            return cards.pop();
        }
        return null;
    }


    public void dispose() {
        for (Card card : cards) {
            card.dispose();
        }
    }
}

