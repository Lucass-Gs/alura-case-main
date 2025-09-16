package br.com.alura.projeto.login;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@ActiveProfiles("test")
class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CourseRepository courseRepository;

    private Category category1;
    private Category category2;
    private Course course1;
    private Course course2;
    private Course course3;

    @BeforeEach
    void setUp() {
        category1 = new Category("Programação", "programacao", "#00C86F", 1);
        category2 = new Category("Front-end", "frontend", "#6BD1FF", 2);

        course1 = new Course();
        course1.setId(1L);
        course1.setName("Spring Boot");
        course1.setCode("spring");
        course1.setInstructor("João Silva");
        course1.setCategory("Programação");
        course1.setDescription("Spring Boot course");
        course1.setStatus(CourseStatus.ACTIVE);
        course1.setCreatedAt(LocalDateTime.now());

        course2 = new Course();
        course2.setId(2L);
        course2.setName("React");
        course2.setCode("react");
        course2.setInstructor("Maria Santos");
        course2.setCategory("Front-end");
        course2.setDescription("React course");
        course2.setStatus(CourseStatus.ACTIVE);
        course2.setCreatedAt(LocalDateTime.now());

        course3 = new Course();
        course3.setId(3L);
        course3.setName("Java");
        course3.setCode("java");
        course3.setInstructor("Pedro Costa");
        course3.setCategory("Programação");
        course3.setDescription("Java course");
        course3.setStatus(CourseStatus.ACTIVE);
        course3.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldReturnLoginPageWithCategoriesAndCourses() throws Exception {
        List<Category> categories = Arrays.asList(category1, category2);
        List<Course> courses = Arrays.asList(course1, course2, course3);

        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(categories);
        when(courseRepository.findAllActiveCourses()).thenReturn(courses);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attributeExists("totalCourses"))
                .andExpect(model().attribute("totalCourses", 3));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    @Test
    void shouldReturnLoginPageWithEmptyData() throws Exception {
        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(Collections.emptyList());
        when(courseRepository.findAllActiveCourses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attributeExists("totalCourses"))
                .andExpect(model().attribute("totalCourses", 0));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    @Test
    void shouldLimitCoursesPerCategoryToFour() throws Exception {
        List<Category> categories = Collections.singletonList(category1);
        List<Course> courses = getCourses();

        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(categories);
        when(courseRepository.findAllActiveCourses()).thenReturn(courses);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attribute("totalCourses", 7));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    private List<Course> getCourses() {
        List<Course> courses = Arrays.asList(
                course1, course3,
                new Course("Course 1", "c1", "Instructor 1", "Programação", "Description 1"),
                new Course("Course 2", "c2", "Instructor 2", "Programação", "Description 2"),
                new Course("Course 3", "c3", "Instructor 3", "Programação", "Description 3"),
                new Course("Course 4", "c4", "Instructor 4", "Programação", "Description 4"),
                new Course("Course 5", "c5", "Instructor 5", "Programação", "Description 5") // This should be excluded
        );

        courses.forEach(course -> {
            course.setId(System.currentTimeMillis() % 1000000);
            course.setStatus(CourseStatus.ACTIVE);
            course.setCreatedAt(LocalDateTime.now());
        });
        return courses;
    }

    @Test
    void shouldFilterCoursesByCategory() throws Exception {
        List<Category> categories = Arrays.asList(category1, category2);
        List<Course> courses = Arrays.asList(course1, course2, course3);

        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(categories);
        when(courseRepository.findAllActiveCourses()).thenReturn(courses);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attribute("totalCourses", 3));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    @Test
    void shouldHandleCategoriesWithoutCourses() throws Exception {
        List<Category> categories = Arrays.asList(category1, category2);
        List<Course> courses = Collections.singletonList(course1);

        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(categories);
        when(courseRepository.findAllActiveCourses()).thenReturn(courses);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attribute("totalCourses", 1));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    @Test
    void shouldConvertCoursesToDTOs() throws Exception {
        List<Category> categories = Collections.singletonList(category1);
        List<Course> courses = Collections.singletonList(course1);

        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(categories);
        when(courseRepository.findAllActiveCourses()).thenReturn(courses);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attribute("totalCourses", 1));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    @Test
    void shouldHandleNullCourses() throws Exception {
        List<Category> categories = Collections.singletonList(category1);
        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(categories);
        when(courseRepository.findAllActiveCourses()).thenReturn(null);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attributeExists("totalCourses"));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }

    @Test
    void shouldHandleNullCategories() throws Exception {
        List<Course> courses = Collections.singletonList(course1);
        when(categoryRepository.findTop9CategoriesWithActiveCourses()).thenReturn(null);
        when(courseRepository.findAllActiveCourses()).thenReturn(courses);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("categoriesWithCourses"))
                .andExpect(model().attributeExists("totalCourses"));

        verify(categoryRepository).findTop9CategoriesWithActiveCourses();
        verify(courseRepository).findAllActiveCourses();
    }
}
