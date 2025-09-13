package monday.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for Todo.
 * This class tests the Todo class methods to make sure they work correctly.
 */
public class TodoTest {

    /**
     * Test the toString() method of Todo class.
     * This test creates a new Todo and checks if the string representation is correct.
     */
    @Test
    public void testToString() {
        // Step 1: Create a new Todo task
        Todo todo = new Todo("buy groceries");

        // Step 2: Get the string representation
        String result = todo.toString();

        // Step 3: Check if it matches what we expect
        // A new Todo should show [T][ ] (!) followed by the description (with MEDIUM priority by default)
        String expected = "[T][ ] (!) buy groceries";
        assertEquals(expected, result);
    }

    /**
     * Test the toString() method when the task is marked as done.
     */
    @Test
    public void testToString_whenDone() {
        // Step 1: Create a new Todo task
        Todo todo = new Todo("buy groceries");

        // Step 2: Mark it as done
        todo.markAsDone();

        // Step 3: Get the string representation
        String result = todo.toString();

        // Step 4: Check if it shows [X] for completed task with priority
        String expected = "[T][X] (!) buy groceries";
        assertEquals(expected, result);
    }

    /**
     * Test with empty description.
     * Testing edge case to make sure the method handles unusual inputs.
     */
    @Test
    public void testToString_emptyDescription() {
        // Create Todo with empty string
        Todo todo = new Todo("");
        String result = todo.toString();
        String expected = "[T][ ] (!) ";
        assertEquals(expected, result);
    }

    /**
     * Test Todo with HIGH priority.
     */
    @Test
    public void testToString_highPriority() {
        Todo todo = new Todo("urgent task", Task.Priority.HIGH);
        String result = todo.toString();
        String expected = "[T][ ] (!!) urgent task";
        assertEquals(expected, result);
    }

    /**
     * Test Todo with LOW priority.
     */
    @Test
    public void testToString_lowPriority() {
        Todo todo = new Todo("low priority task", Task.Priority.LOW);
        String result = todo.toString();
        String expected = "[T][ ] () low priority task";
        assertEquals(expected, result);
    }

    /**
     * Test priority getter and setter.
     */
    @Test
    public void testPriority() {
        Todo todo = new Todo("test task");

        // Default priority should be MEDIUM
        assertEquals(Task.Priority.MEDIUM, todo.getPriority());

        // Set to HIGH priority
        todo.setPriority(Task.Priority.HIGH);
        assertEquals(Task.Priority.HIGH, todo.getPriority());

        // Verify toString reflects the change
        String expected = "[T][ ] (!!) test task";
        assertEquals(expected, todo.toString());
    }
}
