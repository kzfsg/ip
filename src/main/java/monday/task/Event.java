package monday.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    // Constructor that accepts strings and parses them to LocalDateTime
    public Event(String description, String startTimeStr, String endTimeStr) throws DateTimeParseException {
        super(description);
        this.startDateTime = parseDateTimeFromString(startTimeStr);
        this.endDateTime = parseDateTimeFromString(endTimeStr);

        // Validate that start time is before end time
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
    }

    // Constructor that accepts LocalDateTime directly (for loading from file)
    public Event(String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(description);
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Parses various date/time formats into a LocalDateTime object.
     * Supported formats:
     * - yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)
     * - d/M/yyyy HHmm (e.g., 2/12/2019 1800)
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
                throw new DateTimeParseException("Unable to parse date/time: " + dateTimeStr +
                        ". Supported formats: yyyy-MM-dd HHmm, d/M/yyyy HHmm", dateTimeStr, 0);
            }
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy h:mma");

        String startStr = startDateTime.format(displayFormatter);
        String endStr;

        // Smart formatting: if same day, only show end time; if different day, show full date
        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            endStr = endDateTime.format(DateTimeFormatter.ofPattern("h:mma"));
        } else {
            endStr = endDateTime.format(displayFormatter);
        }

        return "[E]" + getStatusIcon() + " " + description + " (at: " + startStr + " to " + endStr + ")";
    }
}