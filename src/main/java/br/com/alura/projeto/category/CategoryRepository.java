package br.com.alura.projeto.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCode(String code);

    @Query("SELECT c FROM Category c ORDER BY c.order ASC")
    List<Category> findAllOrderedByOrder();
    
    @Query("""
        SELECT DISTINCT c FROM Category c
        INNER JOIN Course co ON c.name = co.category
        WHERE co.status = 'ACTIVE'
        ORDER BY c.order
        """)
    List<Category> findTop9CategoriesWithActiveCourses();
}