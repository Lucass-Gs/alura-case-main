package br.com.alura.projeto.course;

import br.com.alura.projeto.course.CourseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@ActiveProfiles("test")
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    private Course course;
    private NewCourseForm validForm;

    @BeforeEach
    void setUp() {
        course = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        course.setId(1L);

        validForm = new NewCourseForm();
        validForm.setName("Spring Boot");
        validForm.setCode("spring");
        validForm.setInstructor("João Silva");
        validForm.setCategory("Backend");
        validForm.setDescription("Spring Boot course");
    }
    
    @Test
    void shouldReturnCourseListPage() throws Exception {
        List<Course> courses = Collections.singletonList(course);
        when(courseRepository.findAll()).thenReturn(courses);

        mockMvc.perform(get("/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/list"))
                .andExpect(model().attributeExists("courses"));

        verify(courseRepository).findAll();
    }

    @Test
    void shouldReturnNewCourseFormPage() throws Exception {
        mockMvc.perform(get("/admin/course/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));
    }
    
    @Test
    void shouldSaveCourseWithValidData() throws Exception {
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).existsByCode(validForm.getCode());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldCreateCourseWithActiveStatusByDefault() throws Exception {
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course savedCourse = invocation.getArgument(0);
            savedCourse.setId(1L);
            return savedCourse;
        });

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).save(argThat(course -> 
            course.getStatus() == CourseStatus.ACTIVE && 
            course.getInactivationDate() == null
        ));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldReturnFormWithValidationErrorsWhenNameIsBlank(String name) throws Exception {
        mockMvc.perform(post("/admin/course/new")
                .param("name", name)
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldReturnFormWithValidationErrorsWhenInstructorIsBlank(String instructor) throws Exception {
        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", instructor)
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldReturnFormWithValidationErrorsWhenCategoryIsBlank(String category) throws Exception {
        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", category)
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "ab", "spring boot", "very-long-code", "spring-boot"})
    void shouldReturnFormWithValidationErrorsWhenCodeIsInvalid(String invalidCode) throws Exception {
        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", invalidCode)
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldReturnFormWithValidationErrorsWhenDescriptionIsTooLong() throws Exception {
        int charMaxLimit = 501;
        String longDescription = "a".repeat(charMaxLimit);

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", longDescription)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));

        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @Test
    void shouldReturnFormWhenCourseCodeAlreadyExists() throws Exception {
        when(courseRepository.existsByCode(validForm.getCode())).thenReturn(true);

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"));

        verify(courseRepository).existsByCode(validForm.getCode());
        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"java", "spring", "react-js", "node-js", "teste"})
    void shouldAcceptValidCodes(String validCode) throws Exception {
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validCode)
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", validForm.getDescription())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).existsByCode(validCode);
        verify(courseRepository).save(any(Course.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldHandleEmptyOrBlankDescription(String description) throws Exception {
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .param("description", description)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldHandleNullDescription() throws Exception {
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("instructor", validForm.getInstructor())
                .param("category", validForm.getCategory())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).save(any(Course.class));
    }
}
