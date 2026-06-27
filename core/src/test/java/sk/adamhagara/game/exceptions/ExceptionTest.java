package sk.adamhagara.game.exceptions;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExceptionTest {

    // --- TESTY VÝNIMKOV ---

    @Test
    public void testCardValidationException() {
        String cardName = "TestCard";
        String errorMessage = "Test card validation error";
        CardValidationException exception = new CardValidationException(cardName, errorMessage);
        
        assertEquals("Exception message should contain card name", 
                    exception.getMessage(), 
                    String.format("Card '%s' validation failed: %s", cardName, errorMessage));
        assertNotNull("Exception should not be null", exception);
    }

    @Test
    public void testCardValidationExceptionWithCause() {
        String cardName = "TestCard";
        String errorMessage = "Test card validation error";
        Throwable cause = new RuntimeException("Root cause");
        CardValidationException exception = new CardValidationException(cardName, errorMessage, cause);
        
        assertEquals("Exception message should match", 
                    String.format("Card '%s' validation failed: %s", cardName, errorMessage), 
                    exception.getMessage());
        assertEquals("Exception cause should match", cause, exception.getCause());
    }

    @Test
    public void testGameException() {
        String errorMessage = "Test game error";
        GameException exception = new GameException(errorMessage);
        
        assertEquals("Exception message should match", errorMessage, exception.getMessage());
        assertNotNull("Exception should not be null", exception);
    }

    @Test
    public void testGameExceptionWithCause() {
        String errorMessage = "Test game error";
        Throwable cause = new RuntimeException("Root cause");
        GameException exception = new GameException(errorMessage, cause);
        
        assertEquals("Exception message should match", errorMessage, exception.getMessage());
        assertEquals("Exception cause should match", cause, exception.getCause());
    }

    @Test
    public void testExceptionInheritance() {
        String cardName = "TestCard";
        String errorMessage = "test";
        CardValidationException cardException = new CardValidationException(cardName, errorMessage);
        GameException gameException = new GameException("test");
        
        assertTrue("CardValidationException should extend Exception", 
                   cardException instanceof Exception);
        assertTrue("GameException should extend Exception", 
                   gameException instanceof Exception);
    }

    @Test
    public void testExceptionThrowing() {
        try {
            throw new CardValidationException("TestCard", "Test throwing");
        } catch (CardValidationException e) {
            assertEquals("Caught exception message should match", 
                        "Card 'TestCard' validation failed: Test throwing", e.getMessage());
        }
        
        try {
            throw new GameException("Test throwing game");
        } catch (GameException e) {
            assertEquals("Caught game exception message should match", "Test throwing game", e.getMessage());
        }
    }

    @Test
    public void testExceptionStackTrace() {
        try {
            throw new CardValidationException("TestCard", "Stack trace test");
        } catch (CardValidationException e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            assertNotNull("Stack trace should not be null", stackTrace);
            assertTrue("Stack trace should contain this method", 
                       containsMethod(stackTrace, "testExceptionStackTrace"));
        }
    }

    @Test
    public void testCardValidationExceptionGetters() {
        String cardName = "TestCard";
        String errorMessage = "Test error";
        CardValidationException exception = new CardValidationException(cardName, errorMessage);
        
        assertEquals("Card name should match", cardName, exception.getCardName());
        assertEquals("Validation error should match", errorMessage, exception.getValidationError());
    }

    @Test
    public void testExceptionWithNullValues() {
        CardValidationException cardException = new CardValidationException((String)null, (String)null);
        GameException gameException = new GameException((String)null);
        
        assertNotNull("CardValidationException message should not be null", cardException.getMessage());
        assertNull("GameException message should be null", gameException.getMessage());
    }

    @Test
    public void testExceptionWithEmptyString() {
        String emptyMessage = "";
        CardValidationException cardException = new CardValidationException(emptyMessage, emptyMessage);
        GameException gameException = new GameException(emptyMessage);
        
        assertNotNull("CardValidationException message should not be null", cardException.getMessage());
        assertEquals("GameException message should be empty", emptyMessage, gameException.getMessage());
    }

    @Test
    public void testExceptionToString() {
        String cardName = "TestCard";
        String message = "Test toString";
        CardValidationException cardException = new CardValidationException(cardName, message);
        
        String exceptionString = cardException.toString();
        assertNotNull("Exception toString should not be null", exceptionString);
        assertTrue("Exception toString should contain class name", 
                   exceptionString.contains("CardValidationException"));
    }

    // Helper method to check if stack trace contains a specific method
    private boolean containsMethod(StackTraceElement[] stackTrace, String methodName) {
        for (StackTraceElement element : stackTrace) {
            if (element.getMethodName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    // --- ADDITIONAL COMPREHENSIVE TESTS FOR 100% COVERAGE ---

    @Test
    public void testCardValidationExceptionWithCardNameAndCause() {
        String cardName = "TestCard";
        Throwable cause = new RuntimeException("Root cause");
        CardValidationException exception = new CardValidationException(cardName, cause);
        
        assertEquals("Card name should match", cardName, exception.getCardName());
        assertEquals("Validation error should be default", "Unexpected validation error", exception.getValidationError());
        assertEquals("Exception cause should match", cause, exception.getCause());
    }

    @Test
    public void testGameExceptionWithCauseOnly() {
        Throwable cause = new RuntimeException("Root cause");
        GameException exception = new GameException(cause);
        
        assertEquals("Exception cause should match", cause, exception.getCause());
        assertNotNull("Exception should not be null", exception);
    }

    @Test
    public void testCardValidationExceptionWithNullCause() {
        String cardName = "TestCard";
        String errorMessage = "Test error";
        CardValidationException exception = new CardValidationException(cardName, errorMessage, null);
        
        assertEquals("Card name should match", cardName, exception.getCardName());
        assertEquals("Validation error should match", errorMessage, exception.getValidationError());
        assertNull("Exception cause should be null", exception.getCause());
    }

    @Test
    public void testGameExceptionWithNullCause() {
        String errorMessage = "Test error";
        GameException exception = new GameException(errorMessage, null);
        
        assertEquals("Exception message should match", errorMessage, exception.getMessage());
        assertNull("Exception cause should be null", exception.getCause());
    }

    @Test
    public void testCardValidationExceptionGetCardName() {
        String cardName = "AngerCard";
        String errorMessage = "Invalid anger level";
        CardValidationException exception = new CardValidationException(cardName, errorMessage);
        
        assertEquals("Card name should be retrievable", cardName, exception.getCardName());
    }

    @Test
    public void testCardValidationExceptionGetValidationError() {
        String cardName = "TestCard";
        String errorMessage = "Validation failed";
        CardValidationException exception = new CardValidationException(cardName, errorMessage);
        
        assertEquals("Validation error should be retrievable", errorMessage, exception.getValidationError());
    }

    @Test
    public void testGameExceptionLongMessage() {
        String longMessage = "This is a very long error message that should be handled correctly by the exception class without any issues";
        GameException exception = new GameException(longMessage);
        
        assertEquals("Long message should be preserved", longMessage, exception.getMessage());
    }

    @Test
    public void testCardValidationExceptionLongMessage() {
        String cardName = "TestCard";
        String longMessage = "This is a very long validation error message that should be handled correctly";
        CardValidationException exception = new CardValidationException(cardName, longMessage);
        
        assertEquals("Long validation error should be preserved", longMessage, exception.getValidationError());
    }

    @Test
    public void testExceptionMultipleThrows() {
        int throwCount = 0;
        for (int i = 0; i < 5; i++) {
            try {
                throw new CardValidationException("TestCard", "Test error");
            } catch (CardValidationException e) {
                throwCount++;
            }
        }
        assertEquals("Should catch all exceptions", 5, throwCount);
    }

    @Test
    public void testExceptionChaining() {
        Throwable rootCause = new RuntimeException("Root cause");
        Throwable intermediateCause = new GameException("Intermediate", rootCause);
        GameException exception = new GameException("Top level", intermediateCause);
        
        assertEquals("Direct cause should match", intermediateCause, exception.getCause());
        assertEquals("Root cause should match", rootCause, exception.getCause().getCause());
    }
}
