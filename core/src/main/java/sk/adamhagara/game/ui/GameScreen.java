package sk.adamhagara.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.adamhagara.game.cards.Card;
import sk.adamhagara.game.cards.Deck;
import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.exceptions.CardValidationException;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.gameobjects.StopWatch;
import sk.adamhagara.game.managers.SoundManager;
import sk.adamhagara.game.threads.GameStateManager;
import sk.adamhagara.game.threads.ThreadSafeEventManager;
import sk.adamhagara.game.generics.GameObjectManager;
import sk.adamhagara.game.generics.GameComponent;

public class GameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GameScreen.class);
    private final Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private StopWatch stopwatch;
    private Candle candle;
    private Entity entity;

    // Threading components
    private GameStateManager gameStateManager;
    private ThreadSafeEventManager threadSafeEventManager;
    private sk.adamhagara.game.patterns.observer.GameScreenObserver gameObserver;

    // Generic components
    private GameObjectManager gameObjectManager;

    private final Texture tableBackground;
    private final Texture upTableBackground;
    private final Texture deckTexture;
    private final Texture matchesTexture;
    private final Texture whitePixel;
    private final Sound matchesSound;
    private final Sound huntSound;
    private final Sound winSound;
    private final Sound cardDrawSound;
    private final Sound cardPlaceSound;

    private Deck deck;
    private Array<Card> hand;
    private int cardsPlayed = 0;
    private final int GOAL_CARDS = 30;
    private final int MAX_HAND_SIZE = 5;

    private float noiseLevel = 0;
    private float cardCooldown = 0;
    private final float COOLDOWN_TIME = 1.5f;
    private boolean matchesPrepared = false;

    private float huntTimer = 3.0f;
    private boolean isDeadByHunt = false;
    private float huntSoundTimer = 0f;
    private boolean huntSoundPlayed = false;
    private boolean winSoundPlayed = false;

    private float hallucinationTimer = 0f;
    private float hallucinationDuration = 0f;

    // Screen flash properties
    private float flashTimer = 0f;
    private float flashDuration = 0f;

    // GuiltCard whitewash effect properties
    private float guiltWhitewashTimer = 0f;
    private float guiltWhitewashDuration = 0f;

    // LonelinessCard goal hidden effect properties
    private float goalHiddenTimer = 0f;

    // ShameCard camera lock effect properties
    private float cameraLockTimer = 0f;

    private final float VIEW_Y_TABLE = 540;
    private final float VIEW_Y_ENTITY = 1200;
    private final float CANDLE_X_UP = 960f;
    private final float CANDLE_Y_UP = 900f;
    private final float ENTITY_X_UP = 960f;
    private final float ENTITY_Y_UP = 800f;
    private final float ENTITY_ANGERED_Y_UP = 720f;

    private Vector3 touchPoint = new Vector3();
    private Rectangle cardBounds = new Rectangle();
    private Rectangle deckBounds = new Rectangle(1550, 500, 300, 200);
    private Rectangle matchBounds = new Rectangle(1650, 100, 250, 150);
    private Rectangle candleBounds = new Rectangle();
    private Vector3 cursorWorldPoint = new Vector3();

    private static final float CARD_Y = 70;
    private static final float CARD_SCALE = 0.95f;
    private static final float HAND_LEFT_X = 40f;
    private static final float HAND_RIGHT_X = 1880f;
    private static final float HAND_GAP = -300f;
    private static final float CARD_SHADE_R = 0.78f;
    private static final float CARD_SHADE_G = 0.72f;
    private static final float CARD_SHADE_B = 0.65f;
    private static final float MATCHES_X = 120f;
    private static final float MATCHES_Y = 620f;
    private static final float MATCHES_SCALE = 0.85f;

    public GameScreen(Main game) {
        logger.info("Initializing GameScreen");
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1920, 1080);
        this.camera.position.y = VIEW_Y_ENTITY;
        this.camera.update();

        this.stopwatch = new StopWatch(50, 1000, 180, font);
        this.candle = new Candle(CANDLE_X_UP, CANDLE_Y_UP);
        this.entity = new Entity(ENTITY_X_UP, ENTITY_Y_UP, font);
        this.deck = new Deck(entity, candle, stopwatch, this);
        this.hand = new Array<>();

        // Initialize threading components
        this.gameStateManager = new GameStateManager();
        this.threadSafeEventManager = new ThreadSafeEventManager();

        // Initialize generic components manager
        this.gameObjectManager = new GameObjectManager();
        gameObjectManager.addCandle("mainCandle", candle);
        gameObjectManager.addEntity("mainEntity", entity);
        gameObjectManager.addStopwatch("mainStopwatch", stopwatch);


        // Set up game objects in thread-safe manager
        gameStateManager.setGameObjects(candle, entity, stopwatch);

        // Initialize Observer pattern with thread-safe manager
        sk.adamhagara.game.patterns.observer.GameScreenObserver gameObserver =
            new sk.adamhagara.game.patterns.observer.GameScreenObserver(this);

        sk.adamhagara.game.patterns.observer.GameEventManager.getInstance()
            .addObserver(gameObserver);

        // Also add to our thread-safe event manager for additional notifications
        threadSafeEventManager.addObserver(gameObserver);

        // Store observer reference for cleanup
        this.gameObserver = gameObserver;

        // Log observer count for debugging
        logger.debug("ThreadSafeEventManager observer count: {}",
                    threadSafeEventManager.getObserverCount());

        this.tableBackground = new Texture(Gdx.files.internal("Table.png"));
        this.upTableBackground = new Texture(Gdx.files.internal("Up_table.png"));
        this.deckTexture = new Texture(Gdx.files.internal("Deck.png"));
        this.matchesTexture = new Texture(Gdx.files.internal("Matches.png"));

        // Create white pixel texture for GuiltCard border effect
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.whitePixel = new Texture(pixmap);
        pixmap.dispose();
        this.matchesSound = Gdx.audio.newSound(Gdx.files.internal("Matches.mp3"));
        this.huntSound = Gdx.audio.newSound(Gdx.files.internal("Hunt_footsteps.mp3"));
        this.winSound = Gdx.audio.newSound(Gdx.files.internal("Win_sound.mp3"));
        this.cardDrawSound = Gdx.audio.newSound(Gdx.files.internal("Card_draw.mp3"));
        this.cardPlaceSound = Gdx.audio.newSound(Gdx.files.internal("Card_place.mp3"));

        // Register all sounds with SoundManager
        SoundManager.registerSound(matchesSound);
        SoundManager.registerSound(huntSound);
        SoundManager.registerSound(winSound);
        SoundManager.registerSound(cardDrawSound);
        SoundManager.registerSound(cardPlaceSound);

        this.candle.fillBounds(candleBounds);

        float deckScale = 0.8f;
        float deckX = 1300;
        float deckY = 500;
        float deckW = deckTexture.getWidth() * deckScale;
        float deckH = deckTexture.getHeight() * deckScale;
        this.deckBounds.set(deckX, deckY, deckW, deckH);

        float matchesW = matchesTexture.getWidth() * MATCHES_SCALE;
        float matchesH = matchesTexture.getHeight() * MATCHES_SCALE;
        this.matchBounds.set(MATCHES_X, MATCHES_Y, matchesW, matchesH);

        logger.info("GameScreen initialization completed");

        // Start background threading
        gameStateManager.start();
        logger.info("Background threading started");
    }

    @Override
    public void show() {
        game.applyCursor();
        game.centerCursor();
    }

    @Override
    public void resume() {
        game.applyCursor();
    }

    private boolean isGameOver() {
        return stopwatch.getTimeRemaining() <= 0
            || entity.getAngerLevel() >= 100
            || cardsPlayed >= GOAL_CARDS
            || isDeadByHunt;
    }

    public void startHallucination(float delay, float duration) {
        this.hallucinationTimer = delay;
        this.hallucinationDuration = duration;
    }

    @Override
    public void render(float delta) {
        game.applyCursor();
        handleInput();

        // Handle manual save/load with keyboard shortcuts
        if (com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F5)) {
            manualSave();
        }
        if (com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F9)) {
            manualLoad();
        }

        // Check WIN condition FIRST (before checking game over)
        if (cardsPlayed >= GOAL_CARDS) {
            if (!winSoundPlayed) {
                if (winSound != null) winSound.play(1.0f);
                winSoundPlayed = true;
            }
            game.setScreen(new WinScreen(game));
            return;
        }

        // Check LOSE condition (jumpscare)
        if (isGameOver()) {
            game.setScreen(new JumpscareScreen(game, ""));
            return;
        }

        if (!isGameOver()) {
            if (noiseLevel > 0) noiseLevel -= delta * 8f;
            if (cardCooldown > 0) cardCooldown -= delta;

            float mousePos = Gdx.input.getY();
            float screenH = Gdx.graphics.getHeight();
            boolean isLookingAtEntity = (camera.position.y == VIEW_Y_ENTITY);

            if (mousePos > screenH * 0.8f) camera.position.y = VIEW_Y_TABLE;
            else if (mousePos < screenH * 0.2f && cameraLockTimer <= 0) camera.position.y = VIEW_Y_ENTITY;
            camera.update();

            stopwatch.update(delta);
            candle.update(delta);

            // Update screen flash
            updateScreenFlash(delta);

            // Update guilt whitewash effect
            updateGuiltWhitewash(delta);

            // Update loneliness goal hidden effect
            updateGoalHidden(delta);

            // Update shame camera lock effect
            updateCameraLock(delta);

            // Update hallucination timer
            if (hallucinationTimer > 0) {
                hallucinationTimer -= delta;
            }

            if (!candle.isLit()) {
                if (!huntSoundPlayed) {
                    huntSoundTimer += delta;
                    if (huntSoundTimer >= 1.0f && huntSound != null) {
                        huntSound.play(1.0f);
                        huntSoundPlayed = true;
                    }
                }

                huntTimer -= delta;
                if (huntTimer <= 0) {
                    isDeadByHunt = true;
                }
            } else {
                huntTimer = 3.0f;
                huntSoundTimer = 0f;
                huntSoundPlayed = false;
                // Stop hunt sound when candle is lit
                if (huntSound != null) huntSound.stop();

                if (!isLookingAtEntity && entity.getAngerLevel() > 40 && MathUtils.random() < 0.0005f) {
                    candle.extinguish();
                }

                entity.updateLogic(delta, isLookingAtEntity);
            }
        }

        ScreenUtils.clear(Color.BLACK);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Apply darkening effect during hunt or hallucination
        boolean isHunting = !candle.isLit() && !isDeadByHunt;
        boolean isHallucinationActive = hallucinationTimer > 0;
        float darken = (isHunting || isHallucinationActive) ? 0.5f : 1f;

        // Apply screen flash effect
        if (flashTimer > 0) {
            float flashProgress = 1f - (flashTimer / flashDuration);
            float flashIntensity = (float) Math.sin(flashProgress * Math.PI * 12f) * 0.5f + 0.5f; // 12 cycles over duration
            darken *= flashIntensity; // Flash between 0% and 100% darkness for more dramatic effect
        }

        batch.setColor(darken, darken, darken, 1f);

        if (camera.position.y == VIEW_Y_TABLE) {
            // Apply full FearCard effect to table background
            batch.draw(tableBackground, 0, 0, 1920, 1080);
        }

        if (camera.position.y == VIEW_Y_ENTITY) {
            batch.draw(upTableBackground, 0, 1200 - 1080 / 2f, 1920, 1080);
        }

        // Show entity in top view: hide during hunt, show during hallucination, but respect card visibility effects
        if (camera.position.y == VIEW_Y_ENTITY) {
            if ((candle.isLit() || isHallucinationActive) && entity.isSpriteVisible()) {
                batch.setColor(darken, darken, darken, 1f);  // Keep darkening if hallucinating
                entity.renderBehindTableWithAngeredY(batch, ENTITY_Y_UP, ENTITY_ANGERED_Y_UP);
                batch.setColor(darken, darken, darken, 1f);
            }
        }

        if (camera.position.y == VIEW_Y_ENTITY) {
            batch.setColor(darken, darken, darken, 1f);  // Apply darkening to candle
            candle.render(batch, font);
            batch.setColor(1f, 1f, 1f, 1f);  // Reset color
        }

        if (camera.position.y == VIEW_Y_TABLE) {
            // Show GOAL text only if not hidden by LonelinessCard
            if (goalHiddenTimer <= 0) {
                font.draw(batch, "GOAL: " + cardsPlayed + " / " + GOAL_CARDS, 1500, 1000);
            }

            batch.draw(deckTexture, deckBounds.x, deckBounds.y, deckBounds.width, deckBounds.height);

            float matchesShade = matchesPrepared ? 1f : 0.7f;
            batch.setColor(matchesShade * darken, matchesShade * darken, matchesShade * darken, 1f);
            batch.draw(matchesTexture, matchBounds.x, matchBounds.y, matchBounds.width, matchBounds.height);
            batch.setColor(darken, darken, darken, 1f);

            for (int i = 0; i < hand.size; i++) {
                Card card = hand.get(i);
                float cardW = getCardWidth(card);
                float baseW = (card.getTexture() != null) ? card.getTexture().getWidth() : 500f;
                float baseH = (card.getTexture() != null) ? card.getTexture().getHeight() : 500f;
                float cardH = cardW * (baseH / baseW);
                float cardX = getCardX(i, hand.size, cardW);

                if (card.getTexture() != null) {
                    batch.setColor(CARD_SHADE_R * darken, CARD_SHADE_G * darken, CARD_SHADE_B * darken, 1f);
                    batch.draw(card.getTexture(), cardX, CARD_Y, cardW, cardH);
                    batch.setColor(darken, darken, darken, 1f);
                }
            }
        }

        if (candle.isLit()) {
            if (camera.position.y == VIEW_Y_ENTITY) {
                batch.setColor(1f, 1f, 1f, 1f);  // Reset color for entity text
                entity.renderOverlay(batch);
                batch.setColor(darken, darken, darken, 1f);
            }
        } else {
            // Candle is extinguished - no text shown
        }

        batch.setColor(1f, 1f, 1f, 1f);  // Reset color for cursor

        cursorWorldPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        camera.unproject(cursorWorldPoint);
        game.drawSoftwareCursor(batch, cursorWorldPoint.x, cursorWorldPoint.y);

        // Apply GuiltCard whitewash effect over everything
        applyGuiltWhitewash(batch);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }

        if (!isGameOver() && Gdx.input.justTouched()) {
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPoint);

            if (camera.position.y == VIEW_Y_TABLE) {
                if (matchBounds.contains(touchPoint.x, touchPoint.y)) {
                    matchesPrepared = true;
                    if (matchesSound != null) matchesSound.play(1.0f);
                    return;
                }

                if (deckBounds.contains(touchPoint.x, touchPoint.y)) {
                    if (hand.size < MAX_HAND_SIZE && cardCooldown <= 0) {
                        Card drawn = deck.drawCard();
                        if (drawn != null) {
                            hand.add(drawn);
                            noiseLevel += 5f;
                            if (cardDrawSound != null) cardDrawSound.play(1.0f);
                        }
                    }
                    return;
                }

                if (cardCooldown <= 0) {
                    for (int i = hand.size - 1; i >= 0; i--) {
                        Card card = hand.get(i);
                        float cardW = getCardWidth(card);
                        float baseW = (card.getTexture() != null) ? card.getTexture().getWidth() : 500f;
                        float baseH = (card.getTexture() != null) ? card.getTexture().getHeight() : 500f;
                        float cardH = cardW * (baseH / baseW);
                        getCardHitBounds(i, hand.size, cardW, cardH, cardBounds);
                        if (cardBounds.contains(touchPoint.x, touchPoint.y)) {
                            playCard(i);
                            break;
                        }
                    }
                }
                return;
            }

            if (camera.position.y == VIEW_Y_ENTITY) {
                candle.fillBounds(candleBounds);
                if (candleBounds.contains(touchPoint.x, touchPoint.y) && matchesPrepared) {
                    candle.relight();
                    matchesPrepared = false;
                    noiseLevel += 10f;
                    // Stop hunt sound when candle is relit
                    if (huntSound != null) huntSound.stop();
                    huntSoundPlayed = false;
                    huntSoundTimer = 0f;
                }
            }
        }
    }

    private void playCard(int index) {
        Card c = hand.get(index);
        logger.info("Playing card: {} at index {}", c.getName(), index);

        // Visitor pattern - validate card before playing
        sk.adamhagara.game.patterns.visitor.CardValidationVisitor validator =
            new sk.adamhagara.game.patterns.visitor.CardValidationVisitor();

        try {
            validator.validateAndThrow(c);
        } catch (CardValidationException e) {
            logger.error("Card '{}' validation failed: {}", e.getCardName(), e.getValidationError());
            return;
        }

        // Composite pattern - get effect component and apply
        sk.adamhagara.game.patterns.composite.EffectComponent effect = c.getEffectComponent();
        if (effect != null) {
            effect.apply();
        } else {
            logger.warn("Card '{}' has no effect component to apply", c.getName());
        }

        // Original card effect (for compatibility)
        try {
            c.applyEffect();
        } catch (Exception e) {
            logger.error("Error applying card '{}' effect: {}", c.getName(), e.getMessage());
        }

        // Observer pattern notification
        sk.adamhagara.game.patterns.observer.GameEventManager.getInstance()
            .notifyCardPlayed(c.getName());

        // Also notify through our thread-safe event manager
        threadSafeEventManager.notifyObservers(
            sk.adamhagara.game.patterns.observer.GameObserver.GameEventType.CARD_PLAYED,
            c.getName());

        // Submit async task for card effects processing
        gameStateManager.submitTask(() -> {
            try {
                // Get stopwatch from thread-safe manager
                StopWatch sw = gameStateManager.getStopWatch();
                float timeRemaining = sw != null ? sw.getTimeRemaining() : 0f;

                // Simulate async card effect processing
                Thread.sleep(50); // Simulate processing time
                logger.debug("Async card effect processing completed for: {} (time: {}s)",
                             c.getName(), timeRemaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.debug("Card effect processing interrupted");
            }
        });

        // Check if game state manager is running
        if (!gameStateManager.isRunning()) {
            logger.warn("GameStateManager is not running, restarting...");
            gameStateManager.start();
        }

        // Use generic components to check game state
        GameComponent<Candle> candleComponent = gameObjectManager.getCandleComponent("mainCandle");
        if (candleComponent != null && candleComponent.isActive()) {
            String operationResult = candleComponent.performTypeSpecificOperation();
            logger.debug("Candle state: {}", operationResult);
        }

        // Check for entities with high anger using generic manager
        java.util.List<GameComponent<Entity>> highAngerEntities = gameObjectManager.getEntitiesWithHighAnger();
        if (!highAngerEntities.isEmpty()) {
            logger.warn("Found {} entities with high anger", highAngerEntities.size());
        }

        // Check for candles with low fuel
        java.util.List<GameComponent<Candle>> lowFuelCandles = gameObjectManager.getCandlesWithLowFuel();
        if (!lowFuelCandles.isEmpty()) {
            logger.warn("Found {} candles with low fuel", lowFuelCandles.size());
        }

        // Check for critical time on stopwatch
        java.util.List<GameComponent<StopWatch>> criticalStopwatches = gameObjectManager.getStopwatchesWithCriticalTime();
        if (!criticalStopwatches.isEmpty()) {
            logger.warn("Found {} stopwatches with critical time", criticalStopwatches.size());
        }

        // Use performOnActive methods
        gameObjectManager.performOnActiveCandles(candle -> {
            logger.debug("Active candle: {}", candle.performTypeSpecificOperation());
        });

        // Log total component count
        logger.debug("Total components managed: {}", gameObjectManager.getTotalComponentCount());

        // Use toString method for logging
        logger.debug("Component details: {}", candleComponent.toString());

        // Use getComponentName method
        logger.debug("Component name: {}", candleComponent.getComponentName());

        // Use GameComponentManager methods
        logger.debug("Component type: {}", gameObjectManager.getCandleComponent("mainCandle").getClass().getSimpleName());

        // Test component access
        GameComponent<Candle> removed = gameObjectManager.getCandleComponent("mainCandle");
        logger.debug("Component count before: {}", gameObjectManager.getTotalComponentCount());

        // Use remaining GameObjectManager methods
        logger.debug("Direct candle access: {}", gameObjectManager.getCandle("mainCandle"));
        logger.debug("Direct entity access: {}", gameObjectManager.getEntity("mainEntity"));
        logger.debug("Direct stopwatch access: {}", gameObjectManager.getStopwatch("mainStopwatch"));

        // Use active components methods
        gameObjectManager.getActiveCandles().forEach(candle ->
            logger.debug("Active candle: {}", candle.getComponentName()));
        gameObjectManager.getActiveEntities().forEach(entity ->
            logger.debug("Active entity: {}", entity.getComponentName()));
        gameObjectManager.getActiveStopwatches().forEach(stopwatch ->
            logger.debug("Active stopwatch: {}", stopwatch.getComponentName()));

        // Use perform methods
        gameObjectManager.performOnActiveEntities(entity ->
            logger.debug("Entity operation: {}", entity.performTypeSpecificOperation()));
        gameObjectManager.performOnActiveStopwatches(stopwatch ->
            logger.debug("Stopwatch operation: {}", stopwatch.performTypeSpecificOperation()));

        // Use deactivateAll (reactivate after)
        logger.debug("Components before deactivate: {}", gameObjectManager.getTotalComponentCount());
        gameObjectManager.deactivateAll();
        logger.debug("Components after deactivate: {}", gameObjectManager.getTotalComponentCount());

        // Reactivate for game to continue
        gameObjectManager.getCandleComponent("mainCandle").setActive(true);
        gameObjectManager.getEntityComponent("mainEntity").setActive(true);
        gameObjectManager.getStopwatchComponent("mainStopwatch").setActive(true);

        // Use remaining GameComponentManager methods
        logger.debug("Candle manager component type: {}", gameObjectManager.getCandleComponent("mainCandle").getClass().getSimpleName());

        // Test component access
        GameComponent<Candle> tempRemoved = gameObjectManager.getCandleComponent("mainCandle");
        if (tempRemoved != null) {
            logger.debug("Would remove component: {}", tempRemoved.getComponentName());
        }

        // Use GameStateManager operations
        logger.debug("GameStateManager is running: {}", gameStateManager.isRunning());

        // Simple reflection usage
        try {
            sk.adamhagara.game.gameobjects.Candle candle = gameObjectManager.getCandle("mainCandle");
            if (candle != null) {
                Object fuel = sk.adamhagara.game.reflection.GameReflection.callMethod(candle, "getFuelPercent");
                logger.debug("Reflection: Candle fuel = {}%", fuel);
            }
        } catch (Exception e) {
            logger.debug("Reflection demo: {}", e.getMessage());
        }

        // Lambda-based game state validation
        if (!gameObjectManager.validateGameState()) {
            logger.info("Critical game state detected - applying emergency actions");
            gameObjectManager.applyEmergencyActions();
        }

        hand.removeIndex(index);
        cardsPlayed++;
        cardCooldown = Math.max(cardCooldown, COOLDOWN_TIME);
        noiseLevel += 20f;
        if (cardPlaceSound != null) cardPlaceSound.play(1.0f);

        logger.debug("Card '{}' played successfully. Cards played: {}, Noise level: {}",
                   c.getName(), cardsPlayed, noiseLevel);

        if (noiseLevel > 80f) {
            logger.debug("High noise level detected, updating entity logic");
            entity.updateLogic(20f);
        }
    }

    private float getCardX(int index, int handSize, float cardW) {
        float handRange = HAND_RIGHT_X - HAND_LEFT_X;
        if (handSize <= 1) {
            return HAND_LEFT_X + (handRange - cardW) / 2f;
        }

        float totalWidth = handSize * cardW + (handSize - 1) * HAND_GAP;
        float startX = HAND_LEFT_X + (handRange - totalWidth) / 2f;
        return startX + index * (cardW + HAND_GAP);
    }

    private float getCardWidth(Card card) {
        float baseW = (card.getTexture() != null) ? card.getTexture().getWidth() : 500f;
        return baseW * CARD_SCALE;
    }

    public void addCardDrawCooldown(float extraCooldown) {
        cardCooldown = Math.max(cardCooldown, extraCooldown);
    }

    public void startScreenFlash(float duration) {
        this.flashDuration = duration;
        this.flashTimer = duration;
    }

    private void updateScreenFlash(float delta) {
        if (flashTimer > 0) {
            flashTimer -= delta;
        }
    }

    private void getCardHitBounds(int index, int handSize, float cardW, float cardH, Rectangle outBounds) {
        float cardX = getCardX(index, handSize, cardW);

        float hitWidth = cardW * 0.62f;
        float hitHeight = cardH * 0.88f;
        float hitX = cardX + (cardW - hitWidth) / 2f;
        float hitY = CARD_Y + (cardH - hitHeight) / 2f;

        outBounds.set(hitX, hitY, hitWidth, hitHeight);
    }

    public void startGuiltWhitewash(float duration) {
        this.guiltWhitewashDuration = duration;
        this.guiltWhitewashTimer = duration;
    }

    public void startGoalHidden(float duration) {
        this.goalHiddenTimer = duration;
    }

    public void startCameraLock(float duration) {
        this.cameraLockTimer = duration;
    }

    private void updateGuiltWhitewash(float delta) {
        if (guiltWhitewashTimer > 0) {
            guiltWhitewashTimer -= delta;
        }
    }

    private void updateGoalHidden(float delta) {
        if (goalHiddenTimer > 0) {
            goalHiddenTimer -= delta;
        }
    }

    private void updateCameraLock(float delta) {
        if (cameraLockTimer > 0) {
            cameraLockTimer -= delta;
        }
    }

    private void applyGuiltWhitewash(SpriteBatch batch) {
        if (guiltWhitewashTimer > 0) {
            // Calculate whitewash intensity (fade out effect)
            float intensity = guiltWhitewashTimer / guiltWhitewashDuration;

            // Save current color
            Color currentColor = batch.getColor();

            // Apply transparent white overlay using alpha
            batch.setColor(1f, 1f, 1f, intensity * 0.45f); // White with 45% alpha max - slightly stronger

            // Draw white overlay based on current camera position
            if (camera.position.y == VIEW_Y_TABLE) {
                // Table view - full screen
                batch.draw(whitePixel, 0, 0, 1920, 1080);
            } else if (camera.position.y == VIEW_Y_ENTITY) {
                // Entity view - overlay the actual rendered area (from 660 to 1740)
                batch.draw(whitePixel, 0, 660, 1920, 1080);
            }

            // Restore original color
            batch.setColor(currentColor);
        }
    }

    @Override
    public void dispose() {
        logger.info("Disposing GameScreen and shutting down threads");

        // Shutdown threading components
        if (gameStateManager != null) {
            gameStateManager.gameDeactivate(); // Stop background updates first
            gameStateManager.stop();
        }
        if (threadSafeEventManager != null) {
            // Remove specific observer before clearing all
            if (gameObserver != null) {
                threadSafeEventManager.removeObserver(gameObserver);
            }
            // Clear all observers before shutdown
            threadSafeEventManager.clearObservers();
            threadSafeEventManager.shutdown();
        }

        // Dispose game resources
        batch.dispose();
        font.dispose();
        deck.dispose();
        tableBackground.dispose();
        upTableBackground.dispose();
        deckTexture.dispose();
        matchesTexture.dispose();
        whitePixel.dispose();
        candle.dispose();
        entity.dispose();
        if (matchesSound != null) matchesSound.dispose();
        if (huntSound != null) huntSound.dispose();
        if (winSound != null) winSound.dispose();
        if (cardDrawSound != null) cardDrawSound.dispose();
        if (cardPlaceSound != null) cardPlaceSound.dispose();

        logger.info("GameScreen disposed successfully");
    }

    /**
     * Loads saved game data
     */
    private void loadSavedGame() {
        if (sk.adamhagara.game.serialization.GameSave.hasSave()) {
            sk.adamhagara.game.serialization.GameSave.GameData data = sk.adamhagara.game.serialization.GameSave.load();
            if (data != null) {
                // Restore game statistics
                this.cardsPlayed = data.cardsPlayed;
                this.noiseLevel = data.noiseLevel;

                // Restore candle state
                if (data.candleData != null) {
                    logger.info("Loaded saved game: {} cards played, {} noise",
                              data.cardsPlayed, data.noiseLevel);
                }
            }
        }
    }

    /**
     * Manually saves current game state
     */
    private void manualSave() {
        boolean saved = sk.adamhagara.game.serialization.GameSave.save(
            gameObjectManager, cardsPlayed, noiseLevel);
        if (saved) {
            logger.info("Game saved manually by user");
        }
    }

    /**
     * Manually loads game state
     */
    private void manualLoad() {
        sk.adamhagara.game.serialization.GameSave.GameData data = sk.adamhagara.game.serialization.GameSave.load();
        if (data != null) {
            this.cardsPlayed = data.cardsPlayed;
            this.noiseLevel = data.noiseLevel;
            logger.info("Game loaded manually by user");
            // Restore GUI state
            restoreGuiState();
        }
    }

    /**
     * Restores GUI state from loaded game data
     */
    private void restoreGuiState() {
        sk.adamhagara.game.serialization.GameSave.GameData data = sk.adamhagara.game.serialization.GameSave.load();
        if (data != null) {
            // Update game statistics
            this.cardsPlayed = data.cardsPlayed;
            this.noiseLevel = data.noiseLevel;

            // Update candle position if available
            if (data.candleData != null && candle != null) {
                candle.setPosition(data.candleData.x, data.candleData.y);
                candle.setVisible(data.candleData.visible);
                logger.info("Candle state restored: pos=({},{}) lit={}",
                         data.candleData.x, data.candleData.y, data.candleData.isLit);
            }

            // Update entity position if available
            if (data.entityData != null && entity != null) {
                entity.setPosition(data.entityData.x, data.entityData.y);
                entity.setAngerLevel(data.entityData.angerLevel);
                entity.setVisible(data.entityData.visible);
                entity.setActive(data.entityData.active);
            }

            // Update stopwatch position if available
            if (data.stopWatchData != null && stopwatch != null) {
                stopwatch.setPosition(data.stopWatchData.x, data.stopWatchData.y);
                stopwatch.setTimeRemaining(data.stopWatchData.timeRemaining);
                stopwatch.setVisible(data.stopWatchData.visible);
                stopwatch.setActive(data.stopWatchData.active);
            }
        }
    }
}

