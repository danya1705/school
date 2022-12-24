package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Add faculty method was invoked");
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFaculty(long id) {
        logger.info("Get faculty method was invoked");
        return facultyRepository.findById(id);
    }

    public Faculty editFacultyInfo(Faculty faculty) {
        logger.info("Edit faculty method was invoked");
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        logger.warn("There is no faculty with id={}",faculty.getId());
        return null;
    }

    public Optional<Faculty> removeFaculty(long id) {
        logger.info("Remove faculty method was invoked");
        Optional<Faculty> deletedFaculty = facultyRepository.findById(id);
        if (deletedFaculty.isPresent()) {
            facultyRepository.deleteById(id);
        } else {
            logger.warn("There is no faculty with id={}", id);
        }
        return deletedFaculty;
    }

    public Collection<Faculty> getFaculties() {
        logger.info("Get all faculties method was invoked");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Get faculties by color method was invoked");
        return facultyRepository.findFacultiesByColorIgnoreCase(color);
    }

    public Collection<Faculty> getFacultiesByNameOrColor(String nameOrColor) {
        logger.info("Get faculties by name or color method was invoked");
        return facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public Collection<Student> getStudents(Faculty faculty) {
        return studentRepository.findStudentsByFaculty(faculty);
    }
}
