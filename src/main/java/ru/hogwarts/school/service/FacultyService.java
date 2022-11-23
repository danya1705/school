package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long currentId = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(currentId);
        faculties.put(currentId, faculty);
        currentId++;
        return faculty;
    }

    public Faculty getFacultyInfo(long id) {
        return faculties.get(id);
    }

    public Faculty editFacultyInfo(Faculty faculty) {
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty removeFaculty(long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getFaculties() {
        return faculties.values();
    }

    public Collection<Faculty> getFaculties(String color) {
        return faculties.values()
                .stream()
                .filter(e -> e.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
