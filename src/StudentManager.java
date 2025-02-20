import java.util.List;

public class StudentManager {
    private final DatabaseManager dbManager;

    public StudentManager() {
        dbManager = new DatabaseManager();
    }

    public void addStudent(Student student) {
        dbManager.addStudent(student);
    }

    public void removeStudent(Student student) {
        dbManager.removeStudent(student.getRollNumber().get());
    }

    public void updateMarks(Student student, int newMarks) {
        dbManager.updateMarks(student.getRollNumber().get(), newMarks);
        student.setMarks(newMarks);
    }

    public List<Student> getAllStudents() {
        return dbManager.getAllStudents();
    }
}
