import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

import static java.awt.Color.*;

public class StudentManagementSystemGUI extends Application {
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private TableView<Student> studentTable = new TableView<>();
    private StudentManager studentManager = new StudentManager();

    @Override

    public void start(Stage primaryStage) {

        Label titleLabel = new Label("Student Management System");

        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.DARKBLUE);
        titleLabel.setEffect(new DropShadow());

        TextField nameField = new TextField();
        nameField.setPromptText("Enter student name");

        TextField rollNumberField = new TextField();
        rollNumberField.setPromptText("Enter roll number");

        TextField marksField = new TextField();
        marksField.setPromptText("Enter marks");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or roll number");

        Button addStudentButton = new Button("Add Student");
        Button removeStudentButton = new Button("Remove Student");
        Button updateMarksButton = new Button("Update Marks");
        Button exportButton = new Button("Export CSV");
        Button sortAscButton = new Button("Sort: Low to High");
        Button sortDescButton = new Button("Sort: High to Low");

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

        TableColumn<Student, Integer> rollColumn = new TableColumn<>("Roll Number");
        rollColumn.setCellValueFactory(cellData -> cellData.getValue().getRollNumber().asObject());

        TableColumn<Student, Integer> marksColumn = new TableColumn<>("Marks");
        marksColumn.setCellValueFactory(cellData -> cellData.getValue().getMarks().asObject());

        TableColumn<Student, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        studentTable.getColumns().addAll(nameColumn, rollColumn, marksColumn, gradeColumn);
        students.setAll(studentManager.getAllStudents());
        studentTable.setItems(students);

        nameColumn.setSortable(true);
        rollColumn.setSortable(true);
        marksColumn.setSortable(true);
        gradeColumn.setSortable(true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterStudents(newValue));

        addStudentButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                int rollNumber = Integer.parseInt(rollNumberField.getText().trim());
                int marks = Integer.parseInt(marksField.getText().trim());

                if (name.isEmpty()) {
                    showError("Name cannot be empty.");
                    return;
                }
                if (marks < 0 || marks > 100) {
                    showError("Marks must be between 0 and 100.");
                    return;
                }
                String grade = Student.calculateGrade(marks);
                Student student = new Student(name, rollNumber, marks, grade);
                studentManager.addStudent(student);
                students.setAll(studentManager.getAllStudents());
                nameField.clear();
                rollNumberField.clear();
                marksField.clear();
            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for roll number and marks.");
            }
        });

        removeStudentButton.setOnAction(e -> {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                studentManager.removeStudent(selectedStudent);
                students.setAll(studentManager.getAllStudents());
            } else {
                showError("Please select a student to remove.");
            }
        });

        updateMarksButton.setOnAction(e -> {
            Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                try {
                    int newMarks = Integer.parseInt(marksField.getText().trim());
                    studentManager.updateMarks(selectedStudent, newMarks);
                    students.setAll(studentManager.getAllStudents());
                    marksField.clear();
                } catch (NumberFormatException ex) {
                    showError("Please enter valid marks.");
                }
            } else {
                showError("Please select a student to update.");
            }
        });

        exportButton.setOnAction(e -> exportDataToCSV());
        sortAscButton.setOnAction(e -> sortStudents(true));
        sortDescButton.setOnAction(e -> sortStudents(false));


        HBox buttonBox = new HBox(10, addStudentButton, removeStudentButton, updateMarksButton, exportButton,sortAscButton, sortDescButton);
        buttonBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, titleLabel, searchField, nameField, rollNumberField, marksField, buttonBox, studentTable);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setTitle("Student Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void filterStudents(String query) {
        if (query.isEmpty()) {
            students.setAll(studentManager.getAllStudents());
            return;
        }
        students.setAll(studentManager.getAllStudents().stream()
                .filter(s -> s.getName().get().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(s.getRollNumber().get()).contains(query))
                .toList());
    }
    private void sortStudents(boolean ascending) {
        Comparator<Student> comparator = Comparator.comparingInt(s -> s.getMarks().get());
        if (!ascending) {
            comparator = comparator.reversed();
        }
        students.sort(comparator);
    }


    private void exportDataToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("Name,Roll Number,Marks,Grade\n");
                for (Student s : students) {
                    writer.append(s.getName().get()).append(",")
                            .append(String.valueOf(s.getRollNumber().get())).append(",")
                            .append(String.valueOf(s.getMarks().get())).append(",")
                            .append(s.getGrade()).append("\n");
                }
            } catch (IOException e) {
                showError("Error saving file.");
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {

        launch(args);

    }
}
