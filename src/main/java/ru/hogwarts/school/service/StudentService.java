package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final HashMap<Long, Student> students = new HashMap<>();
    private long currentId = 0;

    public Student addStudent(Student student) {
        student.setId(currentId);
        students.put(currentId, student);
        currentId++;
        return student;
    }

    public Student getStudentInfo(long id) {
        return students.get(id);
    }

    public Student editStudentInfo(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    public Student removeStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> getStudents() {
        return students.values();
    }

    public Collection<Student> getStudents(int age) {
        return students.values()
                .stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());
    }
}
