package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFaculty(long id) {
        return facultyRepository.findById(id);
    }

    public Faculty editFacultyInfo(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public Optional<Faculty> removeFaculty(long id) {
        Optional<Faculty> deletedFaculty = facultyRepository.findById(id);
        if (deletedFaculty.isPresent()) {
            facultyRepository.deleteById(id);
        }
        return deletedFaculty;
    }

    public Collection<Faculty> getFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findFacultiesByColorIgnoreCase(color);
    }

    public Collection<Faculty> getFacultiesByNameOrColor(String nameOrColor) {
        return facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public Collection<Student> getStudents(Faculty faculty) {
        return studentRepository.findStudentsByFaculty(faculty);
    }
}
