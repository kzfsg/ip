package monday.task;

public class Task {
    // Changed to protected to allow subclass access while maintaining encapsulation
    // This enables subclasses to access core data without breaking the abstraction
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    // Virtual method that subclasses can override for specialized display
    // Base implementation works for simple todos
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
