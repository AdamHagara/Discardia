package sk.adamhagara.game.serialization;

import sk.adamhagara.game.gameobjects.Candle;
import sk.adamhagara.game.gameobjects.Entity;
import sk.adamhagara.game.gameobjects.StopWatch;
import sk.adamhagara.game.generics.GameObjectManager;

import java.io.*;

/**
 * Simple game save/load system using I/O streams
 * Handles serialization of game state
 */
public class GameSave {
    private static final String SAVE_FILE = "gamestate.sav";

    /**
     * Saves current game state
     * @param gameObjectManager Game object manager
     * @param cardsPlayed Number of cards played
     * @param noiseLevel Current noise level
     * @return true if saved successfully
     */
    public static boolean save(GameObjectManager gameObjectManager, int cardsPlayed, float noiseLevel) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(SAVE_FILE))) {

            // Save game statistics
            dos.writeInt(cardsPlayed);
            dos.writeFloat(noiseLevel);
            dos.writeLong(System.currentTimeMillis());

            // Save candle states
            Candle candle = gameObjectManager.getCandle("mainCandle");
            if (candle != null) {
                dos.writeBoolean(true); // Candle exists
                dos.writeFloat(candle.getX());
                dos.writeFloat(candle.getY());
                dos.writeBoolean(candle.isLit());
                dos.writeInt(candle.getFuelPercent());
                dos.writeBoolean(true); // Assume active
                dos.writeBoolean(candle.isVisible());
            } else {
                dos.writeBoolean(false); // No candle
            }

            // Save entity states
            Entity entity = gameObjectManager.getEntity("mainEntity");
            if (entity != null) {
                dos.writeBoolean(true); // Entity exists
                dos.writeFloat(entity.getX());
                dos.writeFloat(entity.getY());
                dos.writeFloat(entity.getAngerLevel());
                dos.writeBoolean(true); // Assume active
                dos.writeBoolean(entity.isVisible());
            } else {
                dos.writeBoolean(false); // No entity
            }

            // Save stopwatch states
            StopWatch stopwatch = gameObjectManager.getStopwatch("mainStopwatch");
            if (stopwatch != null) {
                dos.writeBoolean(true); // Stopwatch exists
                dos.writeFloat(stopwatch.getX());
                dos.writeFloat(stopwatch.getY());
                dos.writeFloat(stopwatch.getTimeRemaining());
                dos.writeBoolean(true); // Assume active
                dos.writeBoolean(stopwatch.isVisible());
            } else {
                dos.writeBoolean(false); // No stopwatch
            }

            System.out.println("Game saved successfully");
            return true;

        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads game state
     * @return Loaded game data or null if failed
     */
    public static GameData load() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(SAVE_FILE))) {

            // Load game statistics
            int cardsPlayed = dis.readInt();
            float noiseLevel = dis.readFloat();
            long timestamp = dis.readLong();

            // Load candle state
            GameData.CandleData candleData = null;
            if (dis.readBoolean()) {
                candleData = new GameData.CandleData(
                    dis.readFloat(),      // x
                    dis.readFloat(),      // y
                    dis.readBoolean(),    // isLit
                    dis.readInt(),        // fuelPercent
                    dis.readBoolean(),    // active
                    dis.readBoolean()     // visible
                );
            }

            // Load entity state
            GameData.EntityData entityData = null;
            if (dis.readBoolean()) {
                entityData = new GameData.EntityData(
                    dis.readFloat(),      // x
                    dis.readFloat(),      // y
                    dis.readFloat(),      // angerLevel
                    dis.readBoolean(),    // active
                    dis.readBoolean()     // visible
                );
            }

            // Load stopwatch state
            GameData.StopWatchData stopWatchData = null;
            if (dis.readBoolean()) {
                stopWatchData = new GameData.StopWatchData(
                    dis.readFloat(),      // x
                    dis.readFloat(),      // y
                    dis.readFloat(),      // timeRemaining
                    dis.readBoolean(),    // active
                    dis.readBoolean()     // visible
                );
            }

            System.out.println("Game loaded successfully");
            return new GameData(cardsPlayed, noiseLevel, timestamp, candleData, entityData, stopWatchData);

        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if save file exists
     */
    public static boolean hasSave() {
        return new File(SAVE_FILE).exists();
    }


    /**
     * Container for loaded game data
     */
    public static class GameData {
        public final int cardsPlayed;
        public final float noiseLevel;
        public final long timestamp;
        public final CandleData candleData;
        public final EntityData entityData;
        public final StopWatchData stopWatchData;

        public GameData(int cardsPlayed, float noiseLevel, long timestamp,
                      CandleData candleData, EntityData entityData, StopWatchData stopWatchData) {
            this.cardsPlayed = cardsPlayed;
            this.noiseLevel = noiseLevel;
            this.timestamp = timestamp;
            this.candleData = candleData;
            this.entityData = entityData;
            this.stopWatchData = stopWatchData;
        }

        public static class CandleData {
            public final float x, y;
            public final boolean isLit;
            public final int fuelPercent;
            public final boolean active, visible;

            public CandleData(float x, float y, boolean isLit, int fuelPercent,
                           boolean active, boolean visible) {
                this.x = x;
                this.y = y;
                this.isLit = isLit;
                this.fuelPercent = fuelPercent;
                this.active = active;
                this.visible = visible;
            }
        }

        public static class EntityData {
            public final float x, y;
            public final float angerLevel;
            public final boolean active, visible;

            public EntityData(float x, float y, float angerLevel,
                           boolean active, boolean visible) {
                this.x = x;
                this.y = y;
                this.angerLevel = angerLevel;
                this.active = active;
                this.visible = visible;
            }
        }

        public static class StopWatchData {
            public final float x, y;
            public final float timeRemaining;
            public final boolean active, visible;

            public StopWatchData(float x, float y, float timeRemaining,
                              boolean active, boolean visible) {
                this.x = x;
                this.y = y;
                this.timeRemaining = timeRemaining;
                this.active = active;
                this.visible = visible;
            }
        }
    }
}
