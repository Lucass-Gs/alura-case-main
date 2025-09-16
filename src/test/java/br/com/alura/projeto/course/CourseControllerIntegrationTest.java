package br.com.alura.projeto.course;

import br.com.alura.projeto.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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

        try {
            java.lang.reflect.Field createdAtField = Course.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(course, LocalDateTime.now());
        } catch (Exception e) {

        }

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
        Page<Course> coursePage = new PageImpl<>(courses);
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(coursePage);

        mockMvc.perform(get("/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/list"))
                .andExpect(model().attributeExists("datagridItems"));

        verify(courseRepository).findAll(any(Pageable.class));
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
    @ValueSource(strings = {"123", "ab", "spring boot", "very-very-very-long-code"})
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
    void shouldReturnEditCourseFormPage() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/course/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/editForm"))
                .andExpect(model().attributeExists("editCourseForm"))
                .andExpect(model().attributeExists("courseId"))
                .andExpect(model().attributeExists("categories"));

        verify(courseRepository).findById(1L);
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldRedirectToCoursesListWhenCourseNotFoundForEdit() throws Exception {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/course/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).findById(999L);
    }

    @Test
    void shouldUpdateCourseWithValidData() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "Spring Boot Updated")
                .param("code", "spring")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Updated Spring Boot course")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).findById(1L);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldUpdateCourseStatusToInactive() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByCode("spring")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course savedCourse = invocation.getArgument(0);
            return savedCourse;
        });

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "Spring Boot")
                .param("code", "spring")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Spring Boot course")
                .param("status", "INACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).save(argThat(course -> 
            course.getStatus() == CourseStatus.INACTIVE && 
            course.getInactivationDate() != null
        ));
    }

    @Test
    void shouldUpdateCourseStatusToActive() throws Exception {
        Course inactiveCourse = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        inactiveCourse.setId(1L);
        inactiveCourse.setStatus(CourseStatus.INACTIVE);
        inactiveCourse.setInactivationDate(LocalDateTime.now());
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(inactiveCourse));
        when(courseRepository.existsByCode("spring")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course savedCourse = invocation.getArgument(0);
            return savedCourse;
        });

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "Spring Boot")
                .param("code", "spring")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Spring Boot course")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).save(argThat(course -> 
            course.getStatus() == CourseStatus.ACTIVE && 
            course.getInactivationDate() == null
        ));
    }

    @Test
    void shouldReturnFormWithValidationErrorsWhenEditingWithInvalidData() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "")
                .param("code", "spring")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Spring Boot course")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/editForm"));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldReturnFormWhenCourseCodeAlreadyExistsInAnotherCourse() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByCode("existing")).thenReturn(true);
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "Spring Boot")
                .param("code", "existing")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Spring Boot course")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/editForm"));

        verify(courseRepository).findById(1L);
        verify(courseRepository).existsByCode("existing");
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldReturnFormWhenCourseCodeIsChangedToExistingCode() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByCode("existing")).thenReturn(true);
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "Spring Boot")
                .param("code", "existing")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Spring Boot course")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/editForm"));

        verify(courseRepository).findById(1L);
        verify(courseRepository).existsByCode("existing");
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldAllowSameCodeForSameCourse() throws Exception {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByCode("spring")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/edit/1")
                .param("name", "Spring Boot")
                .param("code", "spring")
                .param("instructor", "João Silva")
                .param("category", "Backend")
                .param("description", "Spring Boot course")
                .param("status", "ACTIVE")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseRepository).save(any(Course.class));
    }
}
