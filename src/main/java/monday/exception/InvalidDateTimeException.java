package monday.exception;

/**
 * Thrown when date/time parsing fails due to invalid format.
 * Provides guidance on supported date formats.
 */
public class InvalidDateTimeException extends Exception {
    public InvalidDateTimeException(String originalMessage) {
        super(createUserFriendlyMessage(originalMessage));
    }

    private static String createUserFriendlyMessage(String originalMessage) {
        return "Invalid date/time format. Please use one of these formats:\n"
                + "  - yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)\n"
                + "  - d/M/yyyy HHmm (e.g., 2/12/2019 1800)\n"
                + "  - yyyy-MM-dd (e.g., 2019-12-02, defaults to 11:59 PM for deadlines)\n"
                + "Original error: " + originalMessage;
    }
}