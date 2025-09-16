package br.com.alura.projeto.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);
    
    Optional<Course> findByCode(String code);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE'")
    List<Course> findAllActiveCourses();
    
    @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE' AND c.category = :categoryName ORDER BY c.createdAt DESC")
    List<Course> findActiveCoursesByCategory(@Param("categoryName") String categoryName);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'ACTIVE' ORDER BY c.createdAt DESC")
    List<Course> findAllActiveCoursesOrdered();
    
}
