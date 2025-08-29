package monday.task;

/**
 * Represents a generic task with a description and completion status.
 * This is the base class for all task types in the Monday task manager.
 * Subclasses can extend this to provide specialized behavior for different task types.
 */
public class Task {
    /**
     * The description of the task.
     * Protected to allow subclass access while maintaining encapsulation.
     */
    protected String description;
    
    /**
     * The completion status of the task.
     * True if the task has been completed, false otherwise.
     */
    protected boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this task has been completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon for this task.
     * Shows [X] for completed tasks and [ ] for incomplete tasks.
     *
     * @return The status icon string
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns a string representation of this task.
     * The format is "[status icon] [description]".
     * Subclasses can override this method to provide specialized formatting.
     *
     * @return A string representation of this task
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
