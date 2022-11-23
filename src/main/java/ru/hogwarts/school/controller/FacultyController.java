package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Faculty getStudentInfo(@PathVariable Long id) {
        return facultyService.getFacultyInfo(id);
    }

    @PostMapping
    public Faculty addStudent(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public Faculty editStudentInfo(@RequestBody Faculty faculty) {
        return facultyService.editFacultyInfo(faculty);
    }

    @DeleteMapping("{id}")
    public Faculty removeStudent(@PathVariable Long id) {
        return facultyService.removeFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> getFacultiesByColor(@RequestParam(required = false) String color) {
        if (color == null) {
            return facultyService.getFaculties();
        } else {
            return facultyService.getFaculties(color);
        }
    }
}
