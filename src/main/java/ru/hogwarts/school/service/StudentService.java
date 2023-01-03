package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;
    private Integer counter;
    private final Object flag = new Object();

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

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

    public Collection<Student> getLastStudents() {
        logger.info("Get last five students number method was invoked");
        return studentRepository.getLastStudents();
    }

    public Collection<String> getStudentsNamesStartsWithASortedUpperCase() {
        logger.info("Get students which names starts with A-letter");
        return studentRepository.findAll()
                .stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("A"))
                .sorted()
                .toList();
    }

    public OptionalDouble getAverageAge() {
        logger.info("Get students average age method was invoked");
//        return studentRepository.getAverageAge();
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average();
    }

    public void printStudentsByMultipleStreams() {

        List<Student> studentList = studentRepository.findAll();

        new Thread(() -> {
            System.out.println(studentList.get(2));
            System.out.println(studentList.get(3));
        }).start();

        new Thread(() -> {
            System.out.println(studentList.get(4));
            System.out.println(studentList.get(5));
        }).start();

        System.out.println(studentList.get(0));
        System.out.println(studentList.get(1));
    }

    public void printStudentsByMultipleSynchronizedStreams() {

        List<Student> studentList = studentRepository.findAll();
        counter = 0;

        new Thread(() -> {
            printStudent(studentList);
            printStudent(studentList);
        }).start();

        new Thread(() -> {
            printStudent(studentList);
            printStudent(studentList);
        }).start();

        printStudent(studentList);
        printStudent(studentList);

    }

    private void printStudent(List<Student> studentList) {
        synchronized (flag) {
            System.out.println(studentList.get(counter));
            counter++;
        }
    }
}