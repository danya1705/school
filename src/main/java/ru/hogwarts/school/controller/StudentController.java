package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Student getStudentInfo(@PathVariable Long id) {
        return studentService.getStudentInfo(id);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public Student editStudentInfo(@RequestBody Student student) {
        return studentService.editStudentInfo(student);
    }

    @DeleteMapping("{id}")
    public Student removeStudent(@PathVariable Long id) {
        return studentService.removeStudent(id);
    }

    @GetMapping
    public Collection<Student> getStudentsByAge(@RequestParam(required = false) Integer age) {
        if (age == null) {
            return studentService.getStudents();
        } else {
            return studentService.getStudents(age);
        }
    }
}
