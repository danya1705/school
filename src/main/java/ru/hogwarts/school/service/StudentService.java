package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    public Student addStudent(Student student) {
        logger.info("Add student method was invoked");
        student.setId(null);
        if (student.getFaculty() != null) {
            long facultyId = student.getFaculty().getId();
            if (facultyService.getFaculty(facultyId).isEmpty()) {
                logger.warn("There is no faculty with id={}. Students faculty set to null",facultyId);
                student.setFaculty(null);
            }
        }
        return studentRepository.save(student);
    }

    public Optional<Student> getStudent(long id) {
        logger.info("Get student method was invoked");
        return studentRepository.findById(id);
    }

    public Student editStudentInfo(Student student) {
        logger.info("Edit student method was invoked");
        if (studentRepository.existsById(student.getId())) {
            if (student.getFaculty() != null) {
                long facultyId = student.getFaculty().getId();
                if (facultyService.getFaculty(facultyId).isEmpty()) {
                    logger.warn("There is no faculty with id={}. Students faculty set to null", facultyId);
                    student.setFaculty(null);
                }
            }
            return studentRepository.save(student);
        }
        logger.warn("There is no student with id={}",student.getId());
        return null;
    }

    public Optional<Student> removeStudent(long id) {
        logger.info("Remove student method was invoked");
        Optional<Student> deletedStudent = studentRepository.findById(id);
        if (deletedStudent.isPresent()) {
            studentRepository.deleteById(id);
        } else {
            logger.warn("There is no student with id={}",id);
        }
        return deletedStudent;
    }

    public Collection<Student> getStudents() {
        logger.info("Get all students method was invoked");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudents(int age) {
        logger.info("Get students by age method was invoked");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getStudents(int minAge, int maxAge) {
        logger.info("Get students by age interval method was invoked");
        return studentRepository.findByAgeBetween(minAge,maxAge);
    }

    public int getStudentsNumber() {
        logger.info("Get students number method was invoked");
        return studentRepository.getStudentsNumber();
    }

    public double getAverageAge() {
        logger.info("Get students average age method was invoked");
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getLastStudents() {
        logger.info("Get last five students number method was invoked");
        return studentRepository.getLastStudents();
    }

}
