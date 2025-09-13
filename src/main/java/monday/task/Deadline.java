package monday.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a specific deadline.
 * Extends the base Task class to include due date/time functionality.
 * Deadlines are displayed with a [D] prefix and formatted due date.
 */
public class Deadline extends Task {
    private LocalDateTime dueDateTime;

    /**
     * Constructs a new Deadline task with the specified description and due date string.
     * Parses the due date string into a LocalDateTime object.
     *
     * @param description The description of the deadline task
     * @param dueDateTimeStr The due date/time string in supported formats
     * @throws DateTimeParseException If the date string cannot be parsed
     */
    public Deadline(String description, String dueDateTimeStr) throws DateTimeParseException {
        super(description);
        this.dueDateTime = parseDateTimeFromString(dueDateTimeStr);
    }

    /**
     * Constructs a new Deadline task with the specified description and due date.
     * Used when loading from file where LocalDateTime is already parsed.
     *
     * @param description The description of the deadline task
     * @param dueDateTime The due date/time as LocalDateTime object
     */
    public Deadline(String description, LocalDateTime dueDateTime) {
        super(description);
        this.dueDateTime = dueDateTime;
    }

    /**
     * Returns the due date/time of this deadline.
     *
     * @return The due date/time as LocalDateTime
     */
    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    /**
     * Parses various date/time formats into a LocalDateTime object.
     * Supports the following formats:
     * - yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)
     * - d/M/yyyy HHmm (e.g., 2/12/2019 1800)
     * - yyyy-MM-dd (date only, default time to 23:59)
     *
     * @param dateTimeStr the string representation of the date/time
     * @return the corresponding LocalDateTime object
     * @throws DateTimeParseException if the input string cannot be parsed into a valid LocalDateTime
     */
    private LocalDateTime parseDateTimeFromString(String dateTimeStr) throws DateTimeParseException {
        dateTimeStr = dateTimeStr.trim();

        // Try format: yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e1) {
            // Try format: d/M/yyyy HHmm (e.g., 2/12/2019 1800)
            try {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
            } catch (DateTimeParseException e2) {
                // Try format: yyyy-MM-dd (date only, default time to 23:59)
                try {
                    return LocalDateTime.parse(dateTimeStr + " 2359", DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                } catch (DateTimeParseException e3) {
                    throw new DateTimeParseException("Unable to parse date/time: " + dateTimeStr +
                            ". Supported formats: yyyy-MM-dd HHmm, d/M/yyyy HHmm, yyyy-MM-dd", dateTimeStr, 0);
                }
            }
        }
    }

    @Override
    public String toString() {
        // Display in user-friendly format: MMM dd yyyy h:mma
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy h:mma");
        return "[D]" + getStatusIcon() + " " + description + " (by: " + dueDateTime.format(displayFormatter) + ")";
    }
}