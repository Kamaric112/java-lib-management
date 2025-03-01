# JavaFX Project Conventions for Library Management System

## Architecture: Model-View-Controller (MVC)

JavaFX naturally supports MVC architecture, which provides a clean separation of concerns:

- **Model**: Book, User, Loan, and other domain objects
- **View**: FXML files for UI definition
- **Controller**: Java classes that handle user interactions

## Package Structure

com.library
├── app → Application entry point
├── controller → UI controllers
├── model → Domain model classes
├── repository → Data access layer
├── service → Business logic layer
├── util → Helper utilities
└── view → Custom UI components (if needed)

# File Naming Conventions

- **Java Classes**: PascalCase (e.g., `BookController.java`)
- **FXML Files**: lowercase-hyphenated.fxml (e.g., `book-list.fxml`)
- **CSS Files**: lowercase-hyphenated.css (e.g., `library-styles.css`)
- **Images**: lowercase_underscore.png (e.g., `book_cover_placeholder.png`)

## FXML Best Practices

- Use `fx:id` attributes for components that need controller access
- Keep FXML files focused on layout; minimize embedded script
- Use Scene Builder for visual FXML editing
- Define reusable components as separate FXML files

## CSS Styling

- Create a consistent theme with a main CSS file
- Use CSS classes rather than inline styles
- Define color variables for easy theme changes
- Create separate CSS files for complex components

## Resource Management

- Store text in resource bundles for internationalization
- Keep images in `/resources/images/` directory
- Use relative paths with `getResource()` for loading resources

## Data Persistence

- Implement a repository layer for data access
- Consider using:
  - SQLite for simple local storage
  - H2 database for more features
  - File-based storage for portability

## Code Style

- Follow standard Java naming conventions
- Document public APIs with Javadoc
- Use meaningful variable and method names
- Keep methods short and focused on a single task
- Validate user inputs before processing

## Scene Management

- Create a SceneManager utility class to handle view switching
- Pass data between scenes using a context object or service
- Implement consistent navigation patterns

## Error Handling

- Use appropriate exception handling
- Display user-friendly error messages
- Log detailed errors for debugging
- Consider implementing an ErrorHandler utility

## Testing

- Write unit tests for model and service classes
- Create UI tests for critical user journeys
- Mock external dependencies
- Use TestFX for JavaFX-specific testing

## Build and Deployment

- Use Maven or Gradle for dependency management
- Create a standalone executable with JavaFX Packager
- Consider using jlink to create a custom runtime image

## Version Control

- Use Git for source control
- Create a meaningful `.gitignore` file
- Use feature branches for development
- Write descriptive commit messages
