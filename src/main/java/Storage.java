import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles file storage operations for task persistence.
 * Follows Single Responsibility Principle - only handles file I/O operations.
 */
public class Storage {
    private final String filePath;
    private static final String SEPARATOR = " | ";

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath The path to the file where tasks will be stored
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all tasks to file in the specified format.
     * Format: TaskType | Status | Description | [Additional fields]
     *
     * @param tasks The list of tasks to save
     */
    public void save(ArrayList<Task> tasks) {
        try {
            createDataDirectoryIfNotExists();
            FileWriter fw = new FileWriter(filePath);

            for (Task task : tasks) {
                fw.write(formatTaskForFile(task) + System.lineSeparator());
            }

            fw.close();
        } catch (IOException e) {
            // For save operations, we print error but don't throw exception
            // to avoid disrupting the user experience during normal operations
            System.out.println("Warning: Could not save tasks to file: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from file and returns them as an ArrayList.
     * Handles missing file gracefully by returning empty list.
     *
     * @return ArrayList of tasks loaded from file
     * @throws TaskLoadingException If there's an error reading or parsing the file
     */
    public ArrayList<Task> load() throws TaskLoadingException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks; // Return empty list if file doesn't exist
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    Task task = parseTaskFromLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new TaskLoadingException("Data file not found", e);
        } catch (Exception e) {
            throw new TaskLoadingException("Failed to read or parse data file", e);
        }

        return tasks;
    }

    /**
     * Creates the data directory if it doesn't exist.
     * Handles the case where parent directories need to be created.
     */
    private void createDataDirectoryIfNotExists() {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Formats a task object into the file format string.
     * Uses polymorphism to handle different task types appropriately.
     *
     * @param task The task to format
     * @return The formatted string representation
     */
    private String formatTaskForFile(Task task) {
        String status = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T" + SEPARATOR + status + SEPARATOR + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            String dueDateStr = deadline.getDueDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return "D" + SEPARATOR + status + SEPARATOR + task.getDescription() + SEPARATOR + dueDateStr;
        } else if (task instanceof Event) {
            Event event = (Event) task;
            String startTimeStr = event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            String endTimeStr = event.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return "E" + SEPARATOR + status + SEPARATOR + task.getDescription() + SEPARATOR + startTimeStr + SEPARATOR + endTimeStr;
        } else {
            // Base Task class - treat as Todo
            return "T" + SEPARATOR + status + SEPARATOR + task.getDescription();
        }
    }

    /**
     * Parses a line from the file back into a Task object.
     * Implements error handling for corrupted data.
     *
     * @param line The line to parse
     * @return The parsed Task object, or null if parsing fails
     */
    private Task parseTaskFromLine(String line) {
        try {
            String[] parts = line.split("\\" + SEPARATOR.trim() + "\\s*");
            if (parts.length < 3) {
                System.out.println("Skipping corrupted line: " + line);
                return null;
            }

            String taskType = parts[0].trim();
            boolean isDone = "1".equals(parts[1].trim());
            String description = parts[2].trim();

            Task task = null;

            switch (taskType) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        String dueDateTimeStr = parts[3].trim();
                        try {
                            // Parse the stored date format back to LocalDateTime
                            LocalDateTime dueDateTime = LocalDateTime.parse(dueDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            task = new Deadline(description, dueDateTime);
                        } catch (Exception e) {
                            // Log but don't throw - just skip corrupted entries
                            System.out.println("Skipping corrupted deadline date: " + line);
                            return null;
                        }
                    } else {
                        System.out.println("Skipping corrupted deadline: " + line);
                        return null;
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        String startTimeStr = parts[3].trim();
                        String endTimeStr = parts[4].trim();
                        try {
                            // Parse the stored date format back to LocalDateTime
                            LocalDateTime startDateTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            LocalDateTime endDateTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                            task = new Event(description, startDateTime, endDateTime);
                        } catch (Exception e) {
                            // Log but don't throw - just skip corrupted entries
                            System.out.println("Skipping corrupted event dates: " + line);
                            return null;
                        }
                    } else {
                        System.out.println("Skipping corrupted event: " + line);
                        return null;
                    }
                    break;
                default:
                    System.out.println("Unknown task type, skipping: " + line);
                    return null;
            }

            if (task != null && isDone) {
                task.markAsDone();
            }

            return task;

        } catch (Exception e) {
            System.out.println("Error parsing line '" + line + "': " + e.getMessage());
            return null;
        }
    }
}