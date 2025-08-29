package monday.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for TaskList.
 * This class tests the TaskList class methods to make sure they work correctly.
 */
public class TaskListTest {
    
    private TaskList taskList;

    /**
     * This method runs before each test.
     * It creates a fresh TaskList for each test so tests don't interfere with each other.
     */
    @BeforeEach
    public void setUp() {
        taskList = new TaskList(); // Create a new empty TaskList before each test
    }

    /**
     * Test the size() method with an empty TaskList.
     * A new TaskList should have size 0.
     */
    @Test
    public void testSize_emptyList() {
        // Check that a new TaskList has size 0
        assertEquals(0, taskList.size());
    }

    /**
     * Test the size() method after adding one task.
     */
    @Test
    public void testSize_oneTask() {
        // Step 1: Add one task
        Todo todo = new Todo("test task");
        taskList.addTask(todo);
        
        // Step 2: Check that size is now 1
        assertEquals(1, taskList.size());
    }

    /**
     * Test the size() method after adding multiple tasks.
     */
    @Test
    public void testSize_multipleTasks() {
        // Step 1: Add three different tasks
        taskList.addTask(new Todo("task 1"));
        taskList.addTask(new Todo("task 2"));
        taskList.addTask(new Todo("task 3"));
        
        // Step 2: Check that size is 3
        assertEquals(3, taskList.size());
    }

    /**
     * Test the size() method after adding and then removing a task.
     */
    @Test
    public void testSize_addThenRemove() throws Exception {
        // Step 1: Add a task
        taskList.addTask(new Todo("test task"));
        assertEquals(1, taskList.size()); // Should be 1
        
        // Step 2: Remove the task (task number 1)
        taskList.deleteTask(1);
        
        // Step 3: Check that size is back to 0
        assertEquals(0, taskList.size());
    }
}