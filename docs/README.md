# Monday - Task Management Chatbot

A powerful Java-based task management application featuring both command-line interface and modern JavaFX GUI. Monday helps you organize todos, deadlines, and events with intelligent chatbot-style interactions and persistent storage.

![Monday Application Screenshot](screenshot.png)
<!-- Replace with actual screenshot of your application -->

Monday combines the efficiency of CLI commands with the user-friendliness of a graphical chatbot interface, making task management simple and intuitive.

## Quick Start

### Prerequisites
- Java 17 or higher
- Gradle (included via wrapper)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd monday
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew run
   ```

4. **Create standalone JAR (optional)**
   ```bash
   ./gradlew shadowJar
   java -jar build/libs/MONDAY.jar
   ```

## Core Features

### **Adding Todo Tasks**
Create simple tasks without time constraints using the chatbot interface.

Example: `todo read Java textbook`

```
Got it. I've added this task:
  [T][ ] read Java textbook
Now you have 1 tasks in the list.
```

### **Adding Deadlines**
Add tasks with specific due dates and times. The chatbot understands natural deadline expressions.

Example: `deadline submit assignment /by 2024-12-31 2359`

```
Got it. I've added this task:
  [D][ ] submit assignment (by: Dec 31 2024, 11:59 PM)
Now you have 2 tasks in the list.
```

### **Adding Events**
Schedule tasks with start and end times for proper time management.

Example: `event team meeting /from 2024-12-25 1400 /to 2024-12-25 1600`

```
Got it. I've added this task:
  [E][ ] team meeting (from: Dec 25 2024, 2:00 PM to: Dec 25 2024, 4:00 PM)
Now you have 3 tasks in the list.
```

### **Task Management**
Mark tasks as complete, unmark them, or delete tasks entirely through simple commands.

Examples: 
- `mark 1` - Mark first task as completed
- `unmark 2` - Mark second task as incomplete  
- `delete 3` - Remove third task from list

```
Nice! I've marked this task as done:
  [T][X] read Java textbook
```

### **Smart Search**
Find tasks quickly using keyword search across all task descriptions.

Example: `find assignment`

```
Here are the matching tasks in your list:
2.[D][ ] submit assignment (by: Dec 31 2024, 11:59 PM)
```

### **Task Listing**
View all your tasks in an organized format showing type, completion status, and details.

Example: `list`

```
Here are the tasks in your list:
1.[T][X] read Java textbook
2.[D][ ] submit assignment (by: Dec 31 2024, 11:59 PM)
3.[E][ ] team meeting (from: Dec 25 2024, 2:00 PM to: Dec 25 2024, 4:00 PM)
```

### **Priority Support**
Assign priority levels to tasks for better organization and focus.

Example: `todo important task /priority high`

### **Persistent Storage**
All tasks are automatically saved to `./data/monday.txt` and restored when you restart the application.

### **Dual Interface**
Choose between command-line interface for power users or JavaFX GUI for a modern chatbot experience.

### **Help System**
Get assistance anytime with the built-in help command.

Example: `help`

```
Available commands:
- todo <description> - Add a simple task
- deadline <description> /by <date time> - Add task with deadline
- event <description> /from <start> /to <end> - Add event with time range
- list - Show all tasks
- mark <number> - Mark task as completed
- unmark <number> - Mark task as incomplete
- delete <number> - Remove a task
- find <keyword> - Search tasks
- bye - Exit the application
```

## Command Reference

| Command | Description | Format |
|---------|-------------|--------|
| `todo` | Add simple task | `todo <description>` |
| `deadline` | Add task with deadline | `deadline <description> /by <date time>` |
| `event` | Add event with time range | `event <description> /from <start> /to <end>` |
| `list` | Display all tasks | `list` |
| `mark` | Mark task as completed | `mark <task number>` |
| `unmark` | Mark task as incomplete | `unmark <task number>` |
| `delete` | Remove a task | `delete <task number>` |
| `find` | Search tasks by keyword | `find <keyword>` |
| `help` | Show available commands | `help` |
| `bye` | Exit the application | `bye` |

## Date Format

All date and time inputs use the format: `yyyy-MM-dd HHmm`

Examples:
- `2024-12-31 2359` → December 31st, 2024 at 11:59 PM
- `2024-01-15 0900` → January 15th, 2024 at 9:00 AM

## Task Types

### Todo Tasks
Simple tasks without time constraints.
```
[T][ ] read book
[T][X] buy groceries  # completed
```

### Deadline Tasks
Tasks with specific due dates.
```
[D][ ] assignment (by: Dec 31 2024, 11:59 PM)
[D][X] tax filing (by: Apr 15 2024, 5:00 PM)  # completed
```

### Event Tasks
Tasks with start and end times.
```
[E][ ] conference (from: Jan 10 2025, 9:00 AM to: Jan 12 2025, 5:00 PM)
[E][X] vacation (from: Dec 20 2024, 8:00 AM to: Dec 27 2024, 6:00 PM)  # completed
```

## Development

### Building & Testing
```bash
# Run all tests
./gradlew test

# Check code style
./gradlew checkstyleMain checkstyleTest

# Text UI testing
cd text-ui-test
./runtest.sh        # Linux/Mac
runtest.bat         # Windows
```

### Project Structure
```
src/
├── main/java/monday/
│   ├── Monday.java                 # Main application class
│   ├── Launcher.java              # JavaFX application launcher
│   ├── parser/Parser.java          # Command parsing logic
│   ├── task/
│   │   ├── Task.java              # Base task class with priority support
│   │   ├── Todo.java              # Simple tasks
│   │   ├── Deadline.java          # Tasks with deadlines
│   │   ├── Event.java             # Tasks with time ranges
│   │   └── TaskList.java          # Task collection manager
│   ├── storage/Storage.java        # File I/O operations
│   ├── ui/                        # User interface components
│   │   ├── Ui.java               # CLI interface
│   │   ├── Main.java             # JavaFX Application class
│   │   ├── MainWindow.java       # Primary GUI controller
│   │   └── DialogBox.java        # Chat interaction component
│   └── exception/                 # Custom exception classes
├── main/resources/view/           # FXML files for GUI
└── test/java/monday/             # Unit tests
```

### Setup in IntelliJ IDEA

1. Open IntelliJ and select "Open" 
2. Choose the project directory
3. Configure JDK 17:
   - File → Project Structure → Project Settings → Project
   - Set Project SDK to JDK 17
4. Run the application:
   - Locate `src/main/java/monday/Launcher.java`
   - Right-click and select "Run Launcher.main()"

## Contributing

This project uses:
- JUnit 5 for testing
- Checkstyle for code quality
- JavaFX for GUI components
- Gradle for build management

Please ensure all tests pass and code style checks are satisfied before submitting contributions.

## License

This project is developed as part of an academic assignment. Please refer to your institution's academic integrity policies.

---

**Monday - Making task management simple and efficient through intelligent chatbot interaction! 🚀**
