package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getStudentInfo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.getFacultyInfo(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public Faculty addStudent(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editStudentInfo(@RequestBody Faculty faculty) {
        Faculty processedFaculty = facultyService.editFacultyInfo(faculty);
        if (processedFaculty == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(processedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> removeStudent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(facultyService.removeFaculty(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
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
