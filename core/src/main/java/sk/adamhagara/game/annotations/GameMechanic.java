package sk.adamhagara.game.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for game mechanics to categorize and document game features
 * Used for better code organization and understanding
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameMechanic {
    /**
     * Description of the game mechanic
     * @return Mechanic description
     */
    String description();
    
    /**
     * Category of the game mechanic
     * @return Mechanic category
     */
    Category category();
    
    /**
     * Point value for grading purposes
     * @return Point value
     */
    int points() default 1;
    
    /**
     * Categories of game mechanics
     */
    enum Category {
        CARD_PLAY,
        EFFECT,
        ENTITY_BEHAVIOR,
        RESOURCE_MANAGEMENT,
        UI_INTERACTION
    }
}
