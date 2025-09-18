import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import monday.Monday;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Monday monday;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.jpg"));
    private Image mondayImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Monday instance */
    public void setMonday(Monday m) {
        monday = m;
        showWelcomeMessage();
    }

    /**
     * Displays a welcome message when the application starts
     */
    private void showWelcomeMessage() {
        String greeting = "Hello! I'm Monday, your task management assistant.\n"
                + "What can I help you with today?";
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(greeting, mondayImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Monday's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = monday.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, mondayImage)
        );
        userInput.clear();
    }
}