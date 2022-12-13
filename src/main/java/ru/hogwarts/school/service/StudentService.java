package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        if (!facultyRepository.existsById(student.getFaculty().getId())) {
            student.setFaculty(null);
        }
        return studentRepository.save(student);
    }

    public Optional<Student> getStudent(long id) {
        return studentRepository.findById(id);
    }

    public Student editStudentInfo(Student student) {
        if (studentRepository.existsById(student.getId())) {
            if (student.getFaculty() != null && !facultyRepository.existsById(student.getFaculty().getId())) {
                student.setFaculty(null);
            }
            return studentRepository.save(student);
        }
        return null;
    }

    public Optional<Student> removeStudent(long id) {
        Optional<Student> deletedStudent = studentRepository.findById(id);
        if (deletedStudent.isPresent()) {
            studentRepository.deleteById(id);
        }
        return deletedStudent;
    }

    public Collection<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudents(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getStudents(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge,maxAge);
    }

}
