package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private AvatarService avatarService;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private FacultyController facultyController;

    @InjectMocks
    private AvatarController avatarController;

    @InjectMocks
    private StudentController studentController;


    @Test
    void contextLoads() {
        assertThat(facultyController).isNotNull();
        assertThat(studentController).isNotNull();
    }

    @Test
    void getFacultyInfoPositiveTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        Faculty faculty = generateFaculty(id, name, color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFacultyInfoNegativeTest() throws Exception {

        Long id = 1L;

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id))
                .andExpect(status().isNotFound());
    }


    @Test
    void addFacultyTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        Faculty faculty = generateFaculty(id, name, color);

        JSONObject requestObject = new JSONObject();
        requestObject.put("name", name);
        requestObject.put("color", color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFacultyInfoPositiveTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        Faculty faculty = generateFaculty(id, name, color);

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("name", name);
        requestObject.put("color", color);

        when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFacultyInfoNegativeTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        JSONObject requestObject = new JSONObject();
        requestObject.put("id", id);
        requestObject.put("name", name);
        requestObject.put("color", color);

        when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(requestObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeFacultyPositiveTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        Faculty faculty = generateFaculty(id, name, color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void removeFacultyNegativeTest() throws Exception {

        Long id = 1L;

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsPositiveTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";
        Faculty faculty = generateFaculty(id, name, color);

        Collection<Student> students = List.of(
                generateStudent(1L,"name1",11,faculty),
                generateStudent(2L,"name2",22,faculty),
                generateStudent(3L,"name3",33,faculty),
                generateStudent(4L,"name4",44,faculty));

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        when(studentRepository.findStudentsByFaculty(any(Faculty.class))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id + "/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    void getStudentsNegativeTest() throws Exception {

        Long id = 1L;

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id + "/students"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFacultiesByColorTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        Faculty faculty = generateFaculty(id, name, color);

        when(facultyRepository.findFacultiesByColorIgnoreCase(any(String.class))).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color")
                        .param("Color", color))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty))));
    }

    @Test
    void getFacultiesByNameOrColorTest() throws Exception {

        Long id = 1L;
        String name = "Gryffindor";
        String color = "Red";

        Faculty faculty = generateFaculty(id, name, color);

        when(facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(any(String.class), any(String.class))).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/name_or_color")
                        .param("nameOrColor", color))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(faculty))));
    }

    public Faculty generateFaculty(Long id, String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    public Student generateStudent(Long id, String name, int age, Faculty faculty) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);
        return student;
    }
}
