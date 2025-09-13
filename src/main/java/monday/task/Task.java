package monday.task;

/**
 * Represents a generic task with a description and completion status.
 * This is the base class for all task types in the Monday task manager.
 * Subclasses can extend this to provide specialized behavior for different task types.
 */
public class Task {
    /**
     * Enum representing different priority levels for tasks.
     */
    public enum Priority {
        HIGH(1), MEDIUM(2), LOW(3);

        private final int level;

        Priority(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
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
     * The priority level of the task.
     * Defaults to MEDIUM priority if not specified.
     */
    protected Priority priority;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done with MEDIUM priority.
     *
     * @param description The description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.priority = Priority.MEDIUM;
    }

    /**
     * Constructs a new Task with the specified description and priority.
     * The task is initially marked as not done.
     *
     * @param description The description of the task
     * @param priority The priority level of the task
     */
    public Task(String description, Priority priority) {
        this.description = description;
        this.isDone = false;
        this.priority = priority;
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
     * Returns the priority of this task.
     *
     * @return The priority level of the task
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of this task.
     *
     * @param priority The new priority level for the task
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
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
     * Returns the priority icon for this task.
     * Shows (!!) for HIGH, (!) for MEDIUM, and () for LOW priority.
     *
     * @return The priority icon string
     */
    public String getPriorityIcon() {
        switch (priority) {
        case HIGH:
            return "(!!)";
        case MEDIUM:
            return "(!)";
        case LOW:
            return "()";
        default:
            return "(!)";
        }
    }

    /**
     * Returns a string representation of this task.
     * The format is "[status icon] [priority icon] [description]".
     * Subclasses can override this method to provide specialized formatting.
     *
     * @return A string representation of this task
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + getPriorityIcon() + " " + description;
    }
}
