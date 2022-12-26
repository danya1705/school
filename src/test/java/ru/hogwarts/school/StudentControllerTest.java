package ru.hogwarts.school;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    private final Faker faker = new Faker();

    @BeforeEach
    public void beforeEach() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(studentController).isNotNull();
    }

    private Faculty addFaculty(Faculty faculty) {

        ResponseEntity<Faculty> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/faculty/", faculty, Faculty.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty);
        assertThat(responseEntity.getBody().getId()).isNotNull();

        return responseEntity.getBody();
    }

    private Student addStudent(Student student) {

        ResponseEntity<Student> responseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/student/", student, Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(student);
        assertThat(responseEntity.getBody().getId()).isNotNull();

        return responseEntity.getBody();
    }

    @Test
    void createTest() {
        addStudent(generateStudent(addFaculty(generateFaculty())));
    }

    @Test
    void getStudentInfoPositiveTest() {

        Student student = addStudent(generateStudent(addFaculty(generateFaculty())));
        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + student.getId().toString(), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);
    }

    @Test
    void getStudentInfoNegativeTest() {
        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/student/1", Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void editStudentInfoPositiveTest() {

        Student student = addStudent(generateStudent(addFaculty(generateFaculty())));

        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + student.getId().toString(), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);

        Student student2 = generateStudent(addFaculty(generateFaculty()));
        student.setName(student2.getName());
        student.setAge(student2.getAge());
        student.setFaculty(student2.getFaculty());

        responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student", HttpMethod.PUT, new HttpEntity<>(student), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);
    }

    @Test
    void editStudentInfoNegativeTest() {

        Student student = generateStudent(addFaculty(generateFaculty()));
        student.setId(1L);

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student", HttpMethod.PUT, new HttpEntity<>(student), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void removeStudentPositiveTest() {

        Student student = addStudent(generateStudent(addFaculty(generateFaculty())));

        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + student.getId(), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);

        testRestTemplate.delete("http://localhost:" + port + "/student/" + student.getId());

        responseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + student.getId(), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void removeStudentNegativeTest() {

        Student student = generateStudent(null);
        student.setId(1L);

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student" + student.getId(), HttpMethod.DELETE, new HttpEntity<>(student), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void getStudentsByAgeTest() {

        int studentsQuantity = 10;
        Student student;
        List<Student> studentsList = new ArrayList<>();

        for (int i = 0; i < studentsQuantity; i++) {
            student = addStudent(generateStudent(addFaculty(generateFaculty())));
            studentsList.add(student);
        }

        int age = studentsList.get(0).getAge();
        List<Student> expectedStudentsList = studentsList.stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());

        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student/age?age={age}", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        }, age);
        assertThat(responseEntity.getBody()).containsExactlyInAnyOrderElementsOf(expectedStudentsList);
    }

    @Test
    void getStudentsByAgeBetweenTest() {

        int studentsQuantity = 10;
        Student student;
        List<Student> studentsList = new ArrayList<>();

        for (int i = 0; i < studentsQuantity; i++) {
            student = addStudent(generateStudent(addFaculty(generateFaculty())));
            studentsList.add(student);
        }

        int minAge = 13;
        int maxAge = 15;
        List<Student> expectedStudentsList = studentsList.stream()
                .filter(e -> e.getAge() >= minAge && e.getAge() <= maxAge)
                .collect(Collectors.toList());

        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student/age_between?minAge={minAge}&maxAge={maxAge}", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        }, minAge, maxAge);
        assertThat(responseEntity.getBody()).containsExactlyInAnyOrderElementsOf(expectedStudentsList);
    }

    @Test
    void getLastStudentsTest() {

        int studentsQuantity = 10;
        Student student;
        List<Student> studentsList = new ArrayList<>();

        for (int i = 0; i < studentsQuantity; i++) {
            student = addStudent(generateStudent(addFaculty(generateFaculty())));
            studentsList.add(student);
        }

        List<Student> expectedStudentsList = studentsList.stream()
                .sorted(Comparator.comparing(Student::getId,Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());

        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange("http://localhost:" + port + "/student/last-students", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).containsExactlyInAnyOrderElementsOf(expectedStudentsList);
    }

    public Faculty generateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

    public Student generateStudent(Faculty faculty) {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(12, 16));
        student.setFaculty(faculty);
        return student;
    }


}
