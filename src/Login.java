import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Login extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showWelcomeScreen();

    }

    private void showWelcomeScreen() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to the Student Management System");
        welcomeLabel.setFont(new Font("Arial", 20));
        welcomeLabel.setTextFill(Color.DARKBLUE);
        welcomeLabel.setEffect(new DropShadow());
        Button adminLoginBtn = new Button("Login");

        adminLoginBtn.setOnAction(e -> showLoginScreen());

        layout.getChildren().addAll(welcomeLabel, adminLoginBtn);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome");
        primaryStage.show();
    }

    private void showLoginScreen() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label loginLabel = new Label("Admin Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginBtn = new Button("Login");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authenticate(username, password)) {
                showStudentManagementSystem();
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        layout.getChildren().addAll(loginLabel, usernameField, passwordField, loginBtn);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
    }

    private boolean authenticate(String username, String password) {
        return DatabaseManager.verifyAdmin(username, password);
    }

    private void showStudentManagementSystem() {
        StudentManagementSystemGUI mainSystem = new StudentManagementSystemGUI();
        mainSystem.start(primaryStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {

        launch(args);
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.addUser("Abebe", "password123");
        dbManager.addUser("Kebede", "securePass456");
        dbManager.addUser("Redi", "myStrongPassword");

    }
}
