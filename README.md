# Monday - Task Management CLI

A powerful command-line task management application built in Java, designed to help you organize your todos, deadlines, and events efficiently.

## Features

- ✅ **Todo Management** - Create and manage simple tasks
- ⏰ **Deadline Tracking** - Tasks with due dates and times  
- 📅 **Event Scheduling** - Tasks with start and end times
- 🔍 **Search Functionality** - Find tasks by keywords
- 💾 **Persistent Storage** - Automatic saving and loading of tasks
- 📝 **Clean CLI Interface** - Easy-to-use command-line interface

## Quick Start

### Prerequisites
- Java 17 or higher
- Gradle (included via wrapper)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ip
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew run
   ```

4. **Create standalone JAR** (optional)
   ```bash
   ./gradlew shadowJar
   java -jar build/libs/duke.jar
   ```

## Usage

### Basic Commands

| Command | Description | Example |
|---------|-------------|---------|
| `list` | Display all tasks | `list` |
| `todo` | Add a simple task | `todo read book` |
| `deadline` | Add task with deadline | `deadline submit assignment /by 2024-12-31 2359` |
| `event` | Add event with time range | `event team meeting /from 2024-12-25 1400 /to 2024-12-25 1600` |
| `mark` | Mark task as completed | `mark 1` |
| `unmark` | Mark task as incomplete | `unmark 1` |
| `delete` | Remove a task | `delete 2` |
| `find` | Search tasks by keyword | `find book` |
| `help` | Show available commands | `help` |
| `bye` | Exit the application | `bye` |

### Date Format
- Use `yyyy-MM-dd HHmm` format for dates and times
- Example: `2024-12-31 2359` for December 31st, 2024 at 11:59 PM

### Example Session

```
Hello! I'm Monday
What can I do for you?

> todo read Java textbook
Got it. I've added this task:
  [T][ ] read Java textbook
Now you have 1 tasks in the list.

> deadline submit project /by 2024-12-15 2359
Got it. I've added this task:
  [D][ ] submit project (by: Dec 15 2024, 11:59 PM)
Now you have 2 tasks in the list.

> mark 1
Nice! I've marked this task as done:
  [T][X] read Java textbook

> list
Here are the tasks in your list:
1.[T][X] read Java textbook
2.[D][ ] submit project (by: Dec 15 2024, 11:59 PM)
```

## Development

### Project Structure

```
src/
├── main/java/monday/
│   ├── Monday.java                 # Main application class
│   ├── parser/Parser.java          # Command parsing logic
│   ├── task/
│   │   ├── Task.java              # Base task class
│   │   ├── Todo.java              # Simple tasks
│   │   ├── Deadline.java          # Tasks with deadlines
│   │   ├── Event.java             # Tasks with time ranges
│   │   └── TaskList.java          # Task collection manager
│   ├── storage/Storage.java        # File I/O operations
│   ├── ui/Ui.java                 # User interface handling
│   └── exception/                  # Custom exception classes
└── test/java/monday/              # Unit tests
```

### Architecture Highlights

- **Clean Architecture** - Separation of concerns with distinct layers
- **Command Pattern** - Encapsulated command processing
- **Inheritance Hierarchy** - Polymorphic task types
- **Exception Handling** - Comprehensive error management
- **Persistent Storage** - Automatic data persistence

### Building & Testing

```bash
# Run all tests
./gradlew test

# Text UI testing
cd text-ui-test
./runtest.sh        # Linux/Mac
# or
runtest.bat         # Windows

# Build with all checks
./gradlew build
```

### Task Types

#### Todo Tasks
Simple tasks without time constraints.
```
[T][ ] read book
[T][X] buy groceries  # completed
```

#### Deadline Tasks  
Tasks with specific due dates.
```
[D][ ] assignment (by: Dec 31 2024, 11:59 PM)
[D][X] tax filing (by: Apr 15 2024, 5:00 PM)  # completed
```

#### Event Tasks
Tasks with start and end times.
```
[E][ ] conference (from: Jan 10 2025, 9:00 AM to: Jan 12 2025, 5:00 PM)
[E][X] vacation (from: Dec 20 2024, 8:00 AM to: Dec 27 2024, 6:00 PM)  # completed
```

## Data Storage

Tasks are automatically saved to `./data/monday.txt` in a structured format that preserves:
- Task type and completion status
- Descriptions and metadata
- Date/time information for deadlines and events

## Setup in IntelliJ IDEA

### Prerequisites
- JDK 17 or higher
- Latest version of IntelliJ IDEA

### Steps
1. Open IntelliJ (close existing projects if needed)
2. Click `Open` and select the project directory
3. Configure project to use **JDK 17**:
   - Go to File > Project Structure > Project Settings > Project
   - Set Project SDK to JDK 17
   - Set Project language level to `SDK default`
4. Run the application:
   - Locate `src/main/java/monday/Monday.java`
   - Right-click and select `Run Monday.main()`

**Note:** Keep Java files in the `src/main/java` folder structure as expected by Gradle.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is part of an academic assignment. Please refer to your institution's academic integrity policies.

---

**Monday** - Making task management simple and efficient! 🚀
