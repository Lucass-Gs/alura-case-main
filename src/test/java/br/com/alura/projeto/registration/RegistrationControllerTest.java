package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationRepository registrationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Course course;
    private NewRegistrationDTO newRegistration;

    @BeforeEach
    void setUp() {
        user = new User("João Silva", "joao@email.com", Role.STUDENT, "password123");
        course = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        course.setStatus(CourseStatus.ACTIVE);

        newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail("joao@email.com");
        newRegistration.setCourseCode("spring");
    }

    @Test
    @DisplayName("should create registration successfully")
    void shouldCreateRegistrationSuccessfully() throws Exception {
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(courseRepository.findByCode("spring")).thenReturn(Optional.of(course));
        when(registrationRepository.existsByUserAndCourse(user, course)).thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(new Registration(user, course));

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isCreated());

        verify(userRepository).findByEmail("joao@email.com");
        verify(courseRepository).findByCode("spring");
        verify(registrationRepository).existsByUserAndCourse(user, course);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado com o email: joao@email.com"));

        verify(userRepository).findByEmail("joao@email.com");
        verify(courseRepository, never()).findByCode(anyString());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return 404 when course not found")
    void shouldReturn404WhenCourseNotFound() throws Exception {
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(courseRepository.findByCode("spring")).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Curso não encontrado: spring"));

        verify(userRepository).findByEmail("joao@email.com");
        verify(courseRepository).findByCode("spring");
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return 400 when course is inactive")
    void shouldReturn400WhenCourseIsInactive() throws Exception {
        course.setStatus(CourseStatus.INACTIVE);
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(courseRepository.findByCode("spring")).thenReturn(Optional.of(course));

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Não pode se registrar no curso inativo: spring"));

        verify(userRepository).findByEmail("joao@email.com");
        verify(courseRepository).findByCode("spring");
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return 409 when user already registered")
    void shouldReturn409WhenUserAlreadyRegistered() throws Exception {
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(courseRepository.findByCode("spring")).thenReturn(Optional.of(course));
        when(registrationRepository.existsByUserAndCourse(user, course)).thenReturn(true);

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isConflict())
                .andExpect(content().string("O usuário já está registrado nesse curso"));

        verify(userRepository).findByEmail("joao@email.com");
        verify(courseRepository).findByCode("spring");
        verify(registrationRepository).existsByUserAndCourse(user, course);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return 400 when course code is blank")
    void shouldReturn400WhenCourseCodeIsBlank() throws Exception {
        newRegistration.setCourseCode("");

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest());

        verify(userRepository, never()).findByEmail(anyString());
        verify(courseRepository, never()).findByCode(anyString());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return 400 when student email is invalid")
    void shouldReturn400WhenStudentEmailIsInvalid() throws Exception {
        newRegistration.setStudentEmail("invalid-email");

        mockMvc.perform(post("/registration/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest());

        verify(userRepository, never()).findByEmail(anyString());
        verify(courseRepository, never()).findByCode(anyString());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    @DisplayName("should return course registration report")
    void shouldReturnCourseRegistrationReport() throws Exception {

        Object[] course1Data = {"Java para Iniciantes", "java", "Paulo Silveira", "paulo@alura.com", 10L};
        Object[] course2Data = {"Spring Boot", "spring", "João Silva", "joao@alura.com", 8L};
        Object[] course3Data = {"React", "react", "Maria Santos", "maria@alura.com", 5L};
        
        List<Object[]> mockResults = List.of(course1Data, course2Data, course3Data);
        when(registrationRepository.findCourseRegistrationReport()).thenReturn(mockResults);


        mockMvc.perform(get("/registration/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].courseName").value("Java para Iniciantes"))
                .andExpect(jsonPath("$[0].courseCode").value("java"))
                .andExpect(jsonPath("$[0].instructorName").value("Paulo Silveira"))
                .andExpect(jsonPath("$[0].instructorEmail").value("paulo@alura.com"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(10))
                .andExpect(jsonPath("$[1].courseName").value("Spring Boot"))
                .andExpect(jsonPath("$[1].totalRegistrations").value(8))
                .andExpect(jsonPath("$[2].courseName").value("React"))
                .andExpect(jsonPath("$[2].totalRegistrations").value(5));

        verify(registrationRepository).findCourseRegistrationReport();
    }

    @Test
    @DisplayName("should return empty report when no registrations exist")
    void shouldReturnEmptyReportWhenNoRegistrationsExist() throws Exception {

        when(registrationRepository.findCourseRegistrationReport()).thenReturn(new ArrayList<>());


        mockMvc.perform(get("/registration/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(registrationRepository).findCourseRegistrationReport();
    }
}