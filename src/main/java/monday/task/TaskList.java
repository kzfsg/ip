package monday.task;

import java.util.ArrayList;
import monday.exception.InvalidTaskNumberException;
import monday.storage.Storage;

/**
 * Manages a collection of tasks with operations to add, delete, and manipulate tasks.
 * Follows Single Responsibility Principle - only handles task list operations.
 * Automatically saves changes to storage when tasks are modified.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.storage = null;
    }

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks The initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        this.storage = null;
    }

    /**
     * Constructs an empty TaskList with storage for auto-saving.
     *
     * @param storage The storage instance for auto-saving
     */
    public TaskList(Storage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
    }

    /**
     * Constructs a TaskList with an existing list of tasks and storage for auto-saving.
     *
     * @param tasks The initial list of tasks
     * @param storage The storage instance for auto-saving
     */
    public TaskList(ArrayList<Task> tasks, Storage storage) {
        this.tasks = new ArrayList<>(tasks);
        this.storage = storage;
    }

    /**
     * Adds a task to the task list and automatically saves if storage is available.
     *
     * @param task The task to add
     * @throws IllegalArgumentException If task is null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        tasks.add(task);
        autoSave();
    }

    /**
     * Removes a task from the task list at the specified index and automatically saves.
     *
     * @param index The 1-based index of the task to remove
     * @return The task that was removed
     * @throws InvalidTaskNumberException If the index is out of bounds
     */
    public Task deleteTask(int index) throws InvalidTaskNumberException {
        if (index < 1 || index > tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task removed = tasks.remove(index - 1);
        autoSave();
        return removed;
    }

    /**
     * Marks a task as done at the specified index and automatically saves.
     *
     * @param index The 1-based index of the task to mark as done
     * @return The task that was marked as done
     * @throws InvalidTaskNumberException If the index is out of bounds
     */
    public Task markTaskAsDone(int index) throws InvalidTaskNumberException {
        if (index < 1 || index > tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task task = tasks.get(index - 1);
        task.markAsDone();
        autoSave();
        return task;
    }

    /**
     * Marks a task as not done at the specified index and automatically saves.
     *
     * @param index The 1-based index of the task to mark as not done
     * @return The task that was marked as not done
     * @throws InvalidTaskNumberException If the index is out of bounds
     */
    public Task markTaskAsNotDone(int index) throws InvalidTaskNumberException {
        if (index < 1 || index > tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        Task task = tasks.get(index - 1);
        task.markAsNotDone();
        autoSave();
        return task;
    }

    /**
     * Gets a task at the specified index.
     *
     * @param index The 1-based index of the task to retrieve
     * @return The task at the specified index
     * @throws InvalidTaskNumberException If the index is out of bounds
     */
    public Task getTask(int index) throws InvalidTaskNumberException {
        if (index < 1 || index > tasks.size()) {
            throw new InvalidTaskNumberException();
        }
        return tasks.get(index - 1);
    }

    /**
     * Gets the most recently added task.
     *
     * @return The last task in the list
     * @throws IllegalStateException If the task list is empty
     */
    public Task getLastTask() {
        if (tasks.isEmpty()) {
            throw new IllegalStateException("Task list is empty");
        }
        return tasks.get(tasks.size() - 1);
    }

    /**
     * Gets the number of tasks in the task list.
     *
     * @return The size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return True if the task list contains no tasks, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Gets all tasks as an ArrayList for operations like saving to file.
     * Returns a copy to maintain encapsulation.
     *
     * @return A copy of the internal task list
     */
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Replaces the current task list with a new list of tasks.
     * Used when loading tasks from storage.
     *
     * @param newTasks The new list of tasks to set
     * @throws IllegalArgumentException If newTasks is null
     */
    public void setTasks(ArrayList<Task> newTasks) {
        if (newTasks == null) {
            throw new IllegalArgumentException("Task list cannot be null");
        }
        this.tasks = new ArrayList<>(newTasks);
    }

    /**
     * Clears all tasks from the task list.
     */
    public void clear() {
        tasks.clear();
    }

    /**
     * Returns a string representation of all tasks in the list.
     * Each task is numbered starting from 1.
     *
     * @return A formatted string of all tasks, or a message if empty
     */
    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "Your task list is empty.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Automatically saves the task list to storage if storage is available.
     * This method is called after any modification to the task list.
     */
    private void autoSave() {
        if (storage != null) {
            storage.save(tasks);
        }
    }
}