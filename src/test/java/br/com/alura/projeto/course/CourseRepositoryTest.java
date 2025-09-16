package br.com.alura.projeto.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Course activeCourse1;
    private Course activeCourse2;
    private Course inactiveCourse;
    private Course courseWithCategory;

    @BeforeEach
    void setUp() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        
        courseWithCategory = new Course("Java", "java", "Ana Lima", "Backend", "Java course");
        courseWithCategory.setStatus(CourseStatus.ACTIVE);
        courseWithCategory.setCreatedAt(now.minusHours(3));
        entityManager.persistAndFlush(courseWithCategory);
        
        activeCourse1 = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        activeCourse1.setStatus(CourseStatus.ACTIVE);
        activeCourse1.setCreatedAt(now.minusHours(2));
        entityManager.persistAndFlush(activeCourse1);
        
        activeCourse2 = new Course("React", "react", "Maria Santos", "Frontend", "React course");
        activeCourse2.setStatus(CourseStatus.ACTIVE);
        activeCourse2.setCreatedAt(now.minusHours(1));
        entityManager.persistAndFlush(activeCourse2);

        inactiveCourse = new Course("Angular", "angular", "Pedro Costa", "Frontend", "Angular course");
        inactiveCourse.setStatus(CourseStatus.INACTIVE);
        inactiveCourse.setCreatedAt(now.minusHours(4));
        entityManager.persistAndFlush(inactiveCourse);
    }

    @Test
    @DisplayName("should find course by code when course exists")
    void shouldFindCourseByCodeWhenCourseExists() {

        Optional<Course> foundCourse = courseRepository.findByCode("spring");


        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getName()).isEqualTo("Spring Boot");
        assertThat(foundCourse.get().getCode()).isEqualTo("spring");
        assertThat(foundCourse.get().getStatus()).isEqualTo(CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should return empty when course code does not exist")
    void shouldReturnEmptyWhenCourseCodeDoesNotExist() {

        Optional<Course> foundCourse = courseRepository.findByCode("nonexistent");


        assertThat(foundCourse).isEmpty();
    }

    @Test
    @DisplayName("should return true when course code exists")
    void shouldReturnTrueWhenCourseCodeExists() {

        boolean exists = courseRepository.existsByCode("spring");


        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should return false when course code does not exist")
    void shouldReturnFalseWhenCourseCodeDoesNotExist() {

        boolean exists = courseRepository.existsByCode("nonexistent");


        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("should find all active courses")
    void shouldFindAllActiveCourses() {

        List<Course> activeCourses = courseRepository.findAllActiveCourses();


        assertThat(activeCourses).hasSize(3);
        assertThat(activeCourses).extracting(Course::getCode)
                .containsExactlyInAnyOrder("spring", "react", "java");
        assertThat(activeCourses).allMatch(course -> course.getStatus() == CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should find active courses by category")
    void shouldFindActiveCoursesByCategory() {

        List<Course> backendCourses = courseRepository.findActiveCoursesByCategory("Backend");


        assertThat(backendCourses).hasSize(2);
        assertThat(backendCourses).extracting(Course::getCode)
                .containsExactlyInAnyOrder("spring", "java");
        assertThat(backendCourses).allMatch(course -> 
                course.getStatus() == CourseStatus.ACTIVE && 
                course.getCategory().equals("Backend"));
    }

    @Test
    @DisplayName("should find active courses by category when no courses exist")
    void shouldFindActiveCoursesByCategoryWhenNoCoursesExist() {

        List<Course> frontendCourses = courseRepository.findActiveCoursesByCategory("Mobile");


        assertThat(frontendCourses).isEmpty();
    }

    @Test
    @DisplayName("should find all active courses ordered by creation date")
    void shouldFindAllActiveCoursesOrderedByCreationDate() {

        List<Course> activeCourses = courseRepository.findAllActiveCoursesOrdered();


        assertThat(activeCourses).hasSize(3);
        assertThat(activeCourses).extracting(Course::getCode)
                .containsExactly("react", "spring", "java"); // Mais recente primeiro
    }

    @Test
    @DisplayName("should save new course")
    void shouldSaveNewCourse() {

        Course newCourse = new Course("Vue.js", "vue-js", "Carlos Silva", "Frontend", "Vue.js course");
        newCourse.setStatus(CourseStatus.ACTIVE);


        Course savedCourse = courseRepository.save(newCourse);


        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getCode()).isEqualTo("vue-js");
        assertThat(courseRepository.existsByCode("vue-js")).isTrue();
    }

    @Test
    @DisplayName("should update existing course")
    void shouldUpdateExistingCourse() {

        Course course = courseRepository.findByCode("spring").orElseThrow();
        course.setName("Spring Boot Advanced");


        Course updatedCourse = courseRepository.save(course);


        assertThat(updatedCourse.getName()).isEqualTo("Spring Boot Advanced");
        assertThat(updatedCourse.getCode()).isEqualTo("spring");
    }

    @Test
    @DisplayName("should delete course")
    void shouldDeleteCourse() {

        Course course = courseRepository.findByCode("spring").orElseThrow();


        courseRepository.delete(course);


        assertThat(courseRepository.existsByCode("spring")).isFalse();
        assertThat(courseRepository.findByCode("spring")).isEmpty();
    }

    @Test
    @DisplayName("should find course by id")
    void shouldFindCourseById() {

        Course course = courseRepository.findByCode("spring").orElseThrow();


        Optional<Course> foundCourse = courseRepository.findById(course.getId());


        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getCode()).isEqualTo("spring");
    }

    @Test
    @DisplayName("should find all courses")
    void shouldFindAllCourses() {

        List<Course> allCourses = courseRepository.findAll();


        assertThat(allCourses).hasSize(4);
        assertThat(allCourses).extracting(Course::getCode)
                .containsExactlyInAnyOrder("spring", "react", "angular", "java");
    }

    @Test
    @DisplayName("should count courses")
    void shouldCountCourses() {

        long count = courseRepository.count();


        assertThat(count).isEqualTo(4);
    }

    @Test
    @DisplayName("should check if course exists by id")
    void shouldCheckIfCourseExistsById() {

        Course course = courseRepository.findByCode("spring").orElseThrow();


        boolean exists = courseRepository.existsById(course.getId());


        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should check if course does not exist by id")
    void shouldCheckIfCourseDoesNotExistById() {

        boolean exists = courseRepository.existsById(999L);


        assertThat(exists).isFalse();
    }
}

