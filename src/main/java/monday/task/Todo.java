package monday.task;

/**
 * Represents a simple todo task without any date/time constraints.
 * This is the most basic type of task in the Monday task manager.
 * Todo tasks are displayed with a [T] prefix to distinguish them from other task types.
 */
public class Todo extends Task {
    
    /**
     * Constructs a new Todo task with the specified description.
     * The todo is initially marked as not done.
     *
     * @param description The description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this todo task.
     * The format is "[T][status icon] [description]" where:
     * - [T] indicates this is a Todo task
     * - [status icon] shows [X] if done, [ ] if not done
     * - [description] is the task description
     *
     * @return A formatted string representation of this todo task
     */
    @Override
    public String toString() {
        return "[T]" + getStatusIcon() + " " + description;
    }
}
