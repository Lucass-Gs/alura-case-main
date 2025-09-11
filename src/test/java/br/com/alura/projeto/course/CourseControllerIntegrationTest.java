package br.com.alura.projeto.course;

import br.com.alura.projeto.category.CategoryRepository;
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
import java.util.Optional;

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
    
    @MockBean
    private CategoryRepository categoryRepository;

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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        
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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        
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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        
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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        
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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        
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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        
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
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
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

    @Test
    void shouldInactivateCourseSuccessfully() throws Exception {
        Course activeCourse = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        activeCourse.setId(1L);
        activeCourse.setStatus(CourseStatus.ACTIVE);
        
        when(courseRepository.findByCode("spring")).thenReturn(Optional.of(activeCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(activeCourse);

        mockMvc.perform(post("/course/spring/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(courseRepository).findByCode("spring");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldReturnErrorWhenCourseNotFound() throws Exception {
        when(courseRepository.findByCode("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(post("/course/nonexistent/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Curso não encontrado"));

        verify(courseRepository).findByCode("nonexistent");
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldReturnErrorWhenCourseAlreadyInactive() throws Exception {
        Course inactiveCourse = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        inactiveCourse.setId(1L);
        inactiveCourse.setStatus(CourseStatus.INACTIVE);
        
        when(courseRepository.findByCode("spring")).thenReturn(Optional.of(inactiveCourse));

        mockMvc.perform(post("/course/spring/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("status"))
                .andExpect(jsonPath("$.message").value("Curso já está inativo"));

        verify(courseRepository).findByCode("spring");
        verify(courseRepository, never()).save(any(Course.class));
    }
}
