package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    public StudentService(StudentRepository studentRepository, FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        if (facultyService.getFaculty(student.getFaculty().getId()).isEmpty()) {
            student.setFaculty(null);
        }
        return studentRepository.save(student);
    }

    public Optional<Student> getStudent(long id) {
        return studentRepository.findById(id);
    }

    public Student editStudentInfo(Student student) {
        if (studentRepository.existsById(student.getId())) {
            if (student.getFaculty() != null && facultyService.getFaculty(student.getFaculty().getId()).isEmpty()) {
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

    public int getStudentsNumber() {
        return studentRepository.getStudentsNumber();
    }

    public double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getLastStudents() {
        return studentRepository.getLastStudents();
    }

}
