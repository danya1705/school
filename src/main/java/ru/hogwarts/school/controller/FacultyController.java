package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Optional<Faculty> faculty = facultyService.getFaculty(id);
        return faculty
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<Student>> getStudents(@PathVariable Long id) {
        Optional<Faculty> faculty = facultyService.getFaculty(id);
        return faculty
                .map(value -> ResponseEntity.ok(facultyService.getStudents(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping("")
    public ResponseEntity<Faculty> editFacultyInfo(@RequestBody Faculty faculty) {
        Optional<Faculty> processedFaculty = Optional.ofNullable(facultyService.editFacultyInfo(faculty));
        return processedFaculty
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long id) {
        Optional<Faculty> faculty = facultyService.removeFaculty(id);
        return faculty
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public Collection<Faculty> getFaculties() {
        return facultyService.getFaculties();
    }

    @GetMapping(value = "/color")
    public Collection<Faculty> getFacultiesByColor(@RequestParam String Color) {
        return facultyService.getFacultiesByColor(Color);
    }

    @GetMapping(value = "/name_or_color")
    public Collection<Faculty> getFacultiesByNameOrColor(@RequestParam String nameOrColor) {
            return facultyService.getFacultiesByNameOrColor(nameOrColor);
    }

    @GetMapping("/longest-name")
    public ResponseEntity<String> getLongestFacultyName() {
        return facultyService.getLongestFacultyName()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
