package sk.adamhagara.game.annotations;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

public class AnnotationTest {

    @Test
    public void testGameMechanicAnnotationExists() {
        try {
            Class<?> annotationClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic");
            assertNotNull("GameMechanic annotation should exist", annotationClass);
            assertTrue("GameMechanic should be an annotation", annotationClass.isAnnotation());
        } catch (ClassNotFoundException e) {
            fail("GameMechanic annotation should be found");
        }
    }

    @Test
    public void testGameMechanicAnnotationType() {
        try {
            Class<?> annotationClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic");
            assertTrue("GameMechanic should be an annotation interface", annotationClass.isInterface());
            assertTrue("GameMechanic should be an annotation", annotationClass.isAnnotation());
        } catch (ClassNotFoundException e) {
            fail("GameMechanic annotation should be found");
        }
    }

    @Test
    public void testGameMechanicAnnotationUsage() {
        try {
            Class<?> annotationClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic");
            Annotation annotation = annotationClass.getAnnotation(Retention.class);
            assertNotNull("GameMechanic should have Retention annotation", annotation);
        } catch (ClassNotFoundException e) {
            fail("GameMechanic annotation should be found");
        }
    }

    @Test
    public void testGameMechanicRetentionPolicy() {
        try {
            Class<?> annotationClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic");
            Retention retention = annotationClass.getAnnotation(Retention.class);
            assertEquals("GameMechanic should have RUNTIME retention", RetentionPolicy.RUNTIME, retention.value());
        } catch (ClassNotFoundException e) {
            fail("GameMechanic annotation should be found");
        }
    }

    @Test
    public void testGameMechanicTarget() {
        try {
            Class<?> annotationClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic");
            Target target = annotationClass.getAnnotation(Target.class);
            assertNotNull("GameMechanic should have Target annotation", target);
            assertEquals("GameMechanic should target TYPE", ElementType.TYPE, target.value()[0]);
        } catch (ClassNotFoundException e) {
            fail("GameMechanic annotation should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryEnum() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            assertNotNull("Category enum should exist", categoryClass);
            assertTrue("Category should be an enum", categoryClass.isEnum());
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryValues() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            Object[] enumConstants = categoryClass.getEnumConstants();
            assertEquals("Category should have 5 values", 5, enumConstants.length);
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryCardPlay() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            Object[] enumConstants = categoryClass.getEnumConstants();
            boolean hasCardPlay = false;
            for (Object constant : enumConstants) {
                if (constant.toString().equals("CARD_PLAY")) {
                    hasCardPlay = true;
                    break;
                }
            }
            assertTrue("Category should have CARD_PLAY value", hasCardPlay);
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryEffect() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            Object[] enumConstants = categoryClass.getEnumConstants();
            boolean hasEffect = false;
            for (Object constant : enumConstants) {
                if (constant.toString().equals("EFFECT")) {
                    hasEffect = true;
                    break;
                }
            }
            assertTrue("Category should have EFFECT value", hasEffect);
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryEntityBehavior() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            Object[] enumConstants = categoryClass.getEnumConstants();
            boolean hasEntityBehavior = false;
            for (Object constant : enumConstants) {
                if (constant.toString().equals("ENTITY_BEHAVIOR")) {
                    hasEntityBehavior = true;
                    break;
                }
            }
            assertTrue("Category should have ENTITY_BEHAVIOR value", hasEntityBehavior);
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryResourceManagement() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            Object[] enumConstants = categoryClass.getEnumConstants();
            boolean hasResourceManagement = false;
            for (Object constant : enumConstants) {
                if (constant.toString().equals("RESOURCE_MANAGEMENT")) {
                    hasResourceManagement = true;
                    break;
                }
            }
            assertTrue("Category should have RESOURCE_MANAGEMENT value", hasResourceManagement);
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicCategoryUIInteraction() {
        try {
            Class<?> categoryClass = Class.forName("sk.adamhagara.game.annotations.GameMechanic$Category");
            Object[] enumConstants = categoryClass.getEnumConstants();
            boolean hasUIInteraction = false;
            for (Object constant : enumConstants) {
                if (constant.toString().equals("UI_INTERACTION")) {
                    hasUIInteraction = true;
                    break;
                }
            }
            assertTrue("Category should have UI_INTERACTION value", hasUIInteraction);
        } catch (ClassNotFoundException e) {
            fail("Category enum should be found");
        }
    }

    @Test
    public void testGameMechanicAnnotationOnClass() {
        @GameMechanic(description = "Test mechanic", category = GameMechanic.Category.CARD_PLAY, points = 5)
        class TestClass {}
        
        try {
            GameMechanic annotation = TestClass.class.getAnnotation(GameMechanic.class);
            assertNotNull("TestClass should have GameMechanic annotation", annotation);
            assertEquals("Description should match", "Test mechanic", annotation.description());
            assertEquals("Category should match", GameMechanic.Category.CARD_PLAY, annotation.category());
            assertEquals("Points should match", 5, annotation.points());
        } catch (Exception e) {
            fail("GameMechanic annotation should be usable on class");
        }
    }

    @Test
    public void testGameMechanicDefaultPoints() {
        @GameMechanic(description = "Test mechanic", category = GameMechanic.Category.EFFECT)
        class TestClass {}
        
        try {
            GameMechanic annotation = TestClass.class.getAnnotation(GameMechanic.class);
            assertNotNull("TestClass should have GameMechanic annotation", annotation);
            assertEquals("Default points should be 1", 1, annotation.points());
        } catch (Exception e) {
            fail("GameMechanic annotation should have default points");
        }
    }

    @Test
    public void testGameMechanicCategoryToString() {
        GameMechanic.Category category = GameMechanic.Category.CARD_PLAY;
        String categoryString = category.toString();
        assertNotNull("Category toString should not be null", categoryString);
        assertEquals("Category toString should match", "CARD_PLAY", categoryString);
    }

    @Test
    public void testGameMechanicCategoryName() {
        GameMechanic.Category category = GameMechanic.Category.EFFECT;
        String categoryName = category.name();
        assertNotNull("Category name should not be null", categoryName);
        assertEquals("Category name should match", "EFFECT", categoryName);
    }

    @Test
    public void testGameMechanicCategoryOrdinal() {
        GameMechanic.Category category = GameMechanic.Category.ENTITY_BEHAVIOR;
        int ordinal = category.ordinal();
        assertTrue("Category ordinal should be non-negative", ordinal >= 0);
    }

    @Test
    public void testGameMechanicCategoryValuesOf() {
        try {
            GameMechanic.Category category = GameMechanic.Category.valueOf("RESOURCE_MANAGEMENT");
            assertNotNull("Category valueOf should work", category);
            assertEquals("Category should match", GameMechanic.Category.RESOURCE_MANAGEMENT, category);
        } catch (IllegalArgumentException e) {
            fail("Category valueOf should find RESOURCE_MANAGEMENT");
        }
    }
}
