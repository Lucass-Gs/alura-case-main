package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndCourse(User user, Course course);

    @Query("SELECT r FROM Registration r WHERE r.course.status = 'ACTIVE'")
    List<Registration> findAllActiveRegistrations();

    @Query(value = """
        SELECT c.name as courseName, 
               c.code as courseCode, 
               c.instructor as instructorName, 
               c.instructor as instructorEmail, 
               COUNT(r.id) as totalRegistrations
        FROM registration r
        INNER JOIN courses c ON r.course_id = c.id
        WHERE c.status = 'ACTIVE'
        GROUP BY c.id, c.name, c.code, c.instructor
        ORDER BY totalRegistrations DESC
        """, nativeQuery = true)
    List<Object[]> findCourseRegistrationReport();

    @Query("SELECT r FROM Registration r WHERE r.user.email = :email")
    List<Registration> findByUserEmail(@Param("email") String email);

    @Query("SELECT r FROM Registration r WHERE r.course.code = :courseCode")
    List<Registration> findByCourseCode(@Param("courseCode") String courseCode);
}
