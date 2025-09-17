# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Monday is a Java-based task management CLI application with JavaFX GUI support. The application manages three types of tasks: Todos, Deadlines, and Events, with persistent storage and priority support.

## Development Commands

### Building and Running
- **Build project**: `./gradlew build`
- **Run application**: `./gradlew run`
- **Create standalone JAR**: `./gradlew shadowJar` (creates `build/libs/MONDAY.jar`)
- **Run JAR**: `java -jar build/libs/MONDAY.jar`

### Testing
- **Run all tests**: `./gradlew test`
- **Text UI testing**: `cd text-ui-test && ./runtest.sh` (Linux/Mac) or `runtest.bat` (Windows)
- **Check code style**: `./gradlew checkstyleMain checkstyleTest`

### Key Requirements
- Java 17 or higher
- Gradle wrapper is included
- Uses JUnit 5 for testing
- Checkstyle for code quality (version 11.0.0)

## Architecture Overview

### Core Components
1. **Monday.java** - Main application orchestrator, thin coordination layer
2. **Parser** - Command parsing with CommandType enum and execute() method
3. **TaskList** - Task collection management with auto-save to Storage
4. **Storage** - File I/O operations for task persistence (`./data/monday.txt`)
5. **Ui** - User interface handling (both CLI and GUI components)

### Task Hierarchy
- **Task** (abstract base) with Priority enum support
- **Todo** - Simple tasks without time constraints
- **Deadline** - Tasks with due dates (`/by` format)
- **Event** - Tasks with time ranges (`/from` and `/to` format)

### Exception Handling
The application uses specific exceptions in `monday.exception` package:
- `EmptyDescriptionException`, `InvalidCommandFormatException`
- `InvalidDateTimeException`, `InvalidTaskNumberException`
- `TaskLoadingException`, `UnknownCommandException`

### JavaFX GUI Components
- **Launcher.java** - Application entry point
- **Main.java** - JavaFX Application class
- **MainWindow.java** - Primary GUI controller
- **DialogBox.java** - Chat-style interaction component
- FXML files in `src/main/resources/view/`

### Design Patterns
- Single Responsibility Principle applied throughout
- Command pattern in Parser with CommandType enum
- Observer pattern in TaskList with auto-save to Storage
- MVC pattern separation between UI, logic, and data

### Date/Time Format
All date inputs use `yyyy-MM-dd HHmm` format (e.g., `2024-12-31 2359`)

### Data Storage
Tasks are persisted to `./data/monday.txt` with structured format preserving task type, completion status, and metadata.

### Testing Strategy
- Unit tests in `src/test/java/monday/`
- Text UI integration tests in `text-ui-test/`
- JUnit 5 with detailed test logging configured