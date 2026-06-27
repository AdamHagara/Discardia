package sk.adamhagara.game.reflection;

import sk.adamhagara.game.gameobjects.GameObject;

import java.lang.reflect.Method;

/**
 * Simple reflection utility for game objects
 * Enables dynamic method invocation for game operations
 */
public class GameReflection {
    
    /**
     * Calls a method on any game object dynamically
     * @param obj Target game object
     * @param methodName Method name to call
     * @param args Method arguments
     * @return Method result or null for void methods
     */
    public static Object callMethod(GameObject obj, String methodName, Object... args) {
        try {
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }
            
            Method method = obj.getClass().getMethod(methodName, paramTypes);
            return method.invoke(obj, args);
        } catch (Exception e) {
            System.err.println("Reflection error calling " + methodName + ": " + e.getMessage());
            return null;
        }
    }
}
