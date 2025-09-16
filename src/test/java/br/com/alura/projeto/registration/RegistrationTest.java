package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationTest {

    private User user;
    private Course course;

    @BeforeEach
    void setUp() {
        user = new User("João Silva", "joao@email.com", Role.STUDENT, "password123");
        course = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        course.setStatus(CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should create registration with user and course")
    void shouldCreateRegistrationWithUserAndCourse() {
        Registration registration = new Registration(user, course);

        assertThat(registration.getUser()).isEqualTo(user);
        assertThat(registration.getCourse()).isEqualTo(course);
        assertThat(registration.getRegistrationDate()).isNotNull();
        assertThat(registration.getRegistrationDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("should set registration date to current time")
    void shouldSetRegistrationDateToCurrentTime() {
        LocalDateTime beforeCreation = LocalDateTime.now();

        Registration registration = new Registration(user, course);

        assertThat(registration.getRegistrationDate()).isAfterOrEqualTo(beforeCreation);
        assertThat(registration.getRegistrationDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("should have null id before persistence")
    void shouldHaveNullIdBeforePersistence() {
        Registration registration = new Registration(user, course);

        assertThat(registration.getId()).isNull();
    }

    @Test
    @DisplayName("should create registration with different users and courses")
    void shouldCreateRegistrationWithDifferentUsersAndCourses() {
        User user2 = new User("Maria Santos", "maria@email.com", Role.STUDENT, "password456");
        Course course2 = new Course("React", "react", "Maria Santos", "Frontend", "React course");
        course2.setStatus(CourseStatus.ACTIVE);

        Registration registration1 = new Registration(user, course);
        Registration registration2 = new Registration(user2, course2);

        assertThat(registration1.getUser()).isEqualTo(user);
        assertThat(registration1.getCourse()).isEqualTo(course);
        assertThat(registration2.getUser()).isEqualTo(user2);
        assertThat(registration2.getCourse()).isEqualTo(course2);
    }
}

