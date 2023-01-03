package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalDouble;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudent(id);
        return student
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudent(id);
        return student
                .map((value) -> ResponseEntity.ok(value.getFaculty()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("")
    public ResponseEntity<Student> editStudentInfo(@RequestBody Student student) {
        Optional<Student> processedStudent = Optional.ofNullable(studentService.editStudentInfo(student));
        return processedStudent
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> removeStudent(@PathVariable Long id) {
        Optional<Student> student = studentService.removeStudent(id);
        return student
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public Collection<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/age")
    public Collection<Student> getStudentsByAge(@RequestParam Integer age) {
        return studentService.getStudents(age);
    }

    @GetMapping(value = "/age_between")
    public Collection<Student> getStudentsByAgeBetween(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        return studentService.getStudents(minAge, maxAge);
    }

    @GetMapping("/students-number")
    public int getStudentsNumber() {
        return studentService.getStudentsNumber();
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAge() {
        OptionalDouble averageAge = studentService.getAverageAge();
        if (averageAge.isPresent()) {
            return ResponseEntity.ok(averageAge.getAsDouble());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/last-students")
    public Collection<Student> getLastStudents() {
        return studentService.getLastStudents();
    }

    @GetMapping("/names-starts-with-a")
    public Collection<String> getStudentsNamesStartsWithA() {
        return studentService.getStudentsNamesStartsWithASortedUpperCase();
    }

    @GetMapping("/print-students")
    public void printStudents() {
        studentService.printStudentsByMultipleStreams();
    }
    @GetMapping("/print-students-sync")
    public void printStudentsSynchronized() {
        studentService.printStudentsByMultipleSynchronizedStreams();
    }
}
