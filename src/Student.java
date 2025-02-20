import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty rollNumber;
    private SimpleIntegerProperty marks;
    private SimpleStringProperty grade;

    public Student(String name, int rollNumber, int marks, String grade) {
        this.name = new SimpleStringProperty(name);
        this.rollNumber = new SimpleIntegerProperty(rollNumber);
        this.marks = new SimpleIntegerProperty(marks);
        this.grade = new SimpleStringProperty(calculateGrade(marks));
    }

    public static String calculateGrade(int marks) {
        if (marks >= 90) return "A+";
        if (marks >= 85) return "A";
        if (marks >= 80) return "A-";
        if (marks >= 75) return "B+";
        if (marks >= 70) return "B";
        if (marks >= 65) return "B-";
        if (marks >= 60) return "C+";
        if (marks >= 50) return "C";
        return "F"; // Below 50
    }


    public StringProperty getName() {
        return name;
    }

    public IntegerProperty getRollNumber() {
        return rollNumber;
    }

    public IntegerProperty getMarks() {
        return marks;
    }

    public String getGrade() {
        return calculateGrade(marks.get());
    }

    public void setMarks(int marks) {

        this.marks.set(marks);
        this.grade.set(calculateGrade(marks));
    }
}
