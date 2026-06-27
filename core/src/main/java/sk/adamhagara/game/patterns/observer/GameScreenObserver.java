package sk.adamhagara.game.patterns.observer;

import sk.adamhagara.game.ui.GameScreen;

/**
 * Concrete observer for GameScreen
 * Reacts to game state changes and updates the game screen accordingly
 */
public class GameScreenObserver implements GameObserver {
    private final GameScreen gameScreen;

    public GameScreenObserver(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void onGameStateChanged(GameEventType eventType, Object data) {
        switch (eventType) {
            case CANDLE_EXTINGUISHED:
                gameScreen.startScreenFlash(2.0f);
                break;

            case CANDLE_FUEL_LOW:
                Float fuelPercent = (Float) data;
                if (fuelPercent < 20f) {
                    gameScreen.startScreenFlash(0.5f);
                }
                break;

            case CANDLE_FUEL_CRITICAL:
                gameScreen.startScreenFlash(1.5f);
                break;

            case ENTITY_ANGER_HIGH:
                Float angerLevel = (Float) data;
                if (angerLevel > 85f) {
                    gameScreen.startScreenFlash(2.0f);
                }
                break;


            case TIME_WARNING:
                Float timeRemaining = (Float) data;
                if (timeRemaining < 30f) {
                    gameScreen.startScreenFlash(0.8f);
                }
                break;

            case TIME_CRITICAL:
                gameScreen.startScreenFlash(1.8f);
                break;

            default:
                break;
        }
    }
}
