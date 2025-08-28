import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Deadline class - task with due date as LocalDateTime
class Deadline extends Task {
    private LocalDateTime dueDateTime;

    // Constructor that accepts string and parses it to LocalDateTime
    public Deadline(String description, String dueDateTimeStr) throws DateTimeParseException {
        super(description);
        this.dueDateTime = parseDateTimeFromString(dueDateTimeStr);
    }

    // Constructor that accepts LocalDateTime directly (for loading from file)
    public Deadline(String description, LocalDateTime dueDateTime) {
        super(description);
        this.dueDateTime = dueDateTime;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    /**
     * Parse various date/time formats into LocalDateTime
     * Supports: yyyy-MM-dd HHmm, d/M/yyyy HHmm
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