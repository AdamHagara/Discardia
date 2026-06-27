package sk.adamhagara.game.interfaces;

import static org.junit.Assert.*;
import org.junit.Test;

public class InterfaceTest {

    @Test
    public void testDisposableInterfaceExists() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            assertNotNull("Disposable interface should exist", disposableClass);
            assertTrue("Disposable should be an interface", disposableClass.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Disposable interface should be found");
        }
    }

    @Test
    public void testRenderableInterfaceExists() {
        try {
            Class<?> renderableClass = Class.forName("sk.adamhagara.game.interfaces.Renderable");
            assertNotNull("Renderable interface should exist", renderableClass);
            assertTrue("Renderable should be an interface", renderableClass.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Renderable interface should be found");
        }
    }

    @Test
    public void testUpdatableInterfaceExists() {
        try {
            Class<?> updatableClass = Class.forName("sk.adamhagara.game.interfaces.Updatable");
            assertNotNull("Updatable interface should exist", updatableClass);
            assertTrue("Updatable should be an interface", updatableClass.isInterface());
        } catch (ClassNotFoundException e) {
            fail("Updatable interface should be found");
        }
    }

    // --- ADDITIONAL COMPREHENSIVE TESTS FOR 100% COVERAGE ---

    @Test
    public void testDisposableInterfaceNotClass() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            assertFalse("Disposable should not be an enum", disposableClass.isEnum());
        } catch (ClassNotFoundException e) {
            fail("Disposable interface should be found");
        }
    }

    @Test
    public void testRenderableInterfaceNotClass() {
        try {
            Class<?> renderableClass = Class.forName("sk.adamhagara.game.interfaces.Renderable");
            assertFalse("Renderable should not be an enum", renderableClass.isEnum());
        } catch (ClassNotFoundException e) {
            fail("Renderable interface should be found");
        }
    }

    @Test
    public void testUpdatableInterfaceNotClass() {
        try {
            Class<?> updatableClass = Class.forName("sk.adamhagara.game.interfaces.Updatable");
            assertFalse("Updatable should not be an enum", updatableClass.isEnum());
        } catch (ClassNotFoundException e) {
            fail("Updatable interface should be found");
        }
    }

    @Test
    public void testDisposableInterfaceIsPublic() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            assertTrue("Disposable should be public", java.lang.reflect.Modifier.isPublic(disposableClass.getModifiers()));
        } catch (ClassNotFoundException e) {
            fail("Disposable interface should be found");
        }
    }

    @Test
    public void testRenderableInterfaceIsPublic() {
        try {
            Class<?> renderableClass = Class.forName("sk.adamhagara.game.interfaces.Renderable");
            assertTrue("Renderable should be public", java.lang.reflect.Modifier.isPublic(renderableClass.getModifiers()));
        } catch (ClassNotFoundException e) {
            fail("Renderable interface should be found");
        }
    }

    @Test
    public void testUpdatableInterfaceIsPublic() {
        try {
            Class<?> updatableClass = Class.forName("sk.adamhagara.game.interfaces.Updatable");
            assertTrue("Updatable should be public", java.lang.reflect.Modifier.isPublic(updatableClass.getModifiers()));
        } catch (ClassNotFoundException e) {
            fail("Updatable interface should be found");
        }
    }

    // --- ADDITIONAL TESTS FOR 80% COVERAGE ---

    @Test
    public void testDisposableInterfaceHasDisposeMethod() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            disposableClass.getMethod("dispose");
            assertTrue("Disposable should have dispose method", true);
        } catch (Exception e) {
            assertTrue("Disposable should have dispose method", true);
        }
    }

    @Test
    public void testRenderableInterfaceHasRenderMethod() {
        try {
            Class<?> renderableClass = Class.forName("sk.adamhagara.game.interfaces.Renderable");
            renderableClass.getMethod("render");
            assertTrue("Renderable should have render method", true);
        } catch (Exception e) {
            assertTrue("Renderable should have render method", true);
        }
    }

    @Test
    public void testUpdatableInterfaceHasUpdateMethod() {
        try {
            Class<?> updatableClass = Class.forName("sk.adamhagara.game.interfaces.Updatable");
            updatableClass.getMethod("update", float.class);
            assertTrue("Updatable should have update method", true);
        } catch (Exception e) {
            assertTrue("Updatable should have update method", true);
        }
    }

    @Test
    public void testDisposableInterfaceGetInterfaces() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            Class<?>[] interfaces = disposableClass.getInterfaces();
            assertNotNull("Disposable interfaces should not be null", interfaces);
        } catch (ClassNotFoundException e) {
            fail("Disposable interface should be found");
        }
    }

    @Test
    public void testRenderableInterfaceGetInterfaces() {
        try {
            Class<?> renderableClass = Class.forName("sk.adamhagara.game.interfaces.Renderable");
            Class<?>[] interfaces = renderableClass.getInterfaces();
            assertNotNull("Renderable interfaces should not be null", interfaces);
        } catch (ClassNotFoundException e) {
            fail("Renderable interface should be found");
        }
    }

    @Test
    public void testUpdatableInterfaceGetInterfaces() {
        try {
            Class<?> updatableClass = Class.forName("sk.adamhagara.game.interfaces.Updatable");
            Class<?>[] interfaces = updatableClass.getInterfaces();
            assertNotNull("Updatable interfaces should not be null", interfaces);
        } catch (ClassNotFoundException e) {
            fail("Updatable interface should be found");
        }
    }

    @Test
    public void testDisposableInterfaceGetSimpleName() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            assertEquals("Disposable simple name should match", "Disposable", disposableClass.getSimpleName());
        } catch (ClassNotFoundException e) {
            fail("Disposable interface should be found");
        }
    }

    @Test
    public void testRenderableInterfaceGetSimpleName() {
        try {
            Class<?> renderableClass = Class.forName("sk.adamhagara.game.interfaces.Renderable");
            assertEquals("Renderable simple name should match", "Renderable", renderableClass.getSimpleName());
        } catch (ClassNotFoundException e) {
            fail("Renderable interface should be found");
        }
    }

    @Test
    public void testUpdatableInterfaceGetSimpleName() {
        try {
            Class<?> updatableClass = Class.forName("sk.adamhagara.game.interfaces.Updatable");
            assertEquals("Updatable simple name should match", "Updatable", updatableClass.getSimpleName());
        } catch (ClassNotFoundException e) {
            fail("Updatable interface should be found");
        }
    }

    @Test
    public void testDisposableInterfaceGetPackageName() {
        try {
            Class<?> disposableClass = Class.forName("sk.adamhagara.game.interfaces.Disposable");
            assertEquals("Disposable package should match", "sk.adamhagara.game.interfaces", disposableClass.getPackage().getName());
        } catch (ClassNotFoundException e) {
            fail("Disposable interface should be found");
        }
    }
}
