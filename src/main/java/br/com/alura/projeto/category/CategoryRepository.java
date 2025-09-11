package br.com.alura.projeto.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCode(String code);

    @Query("SELECT c FROM Category c ORDER BY c.order ASC")
    List<Category> findAllOrderedByOrder();
    
    @Query(value = """
        SELECT DISTINCT c.* FROM category c
        INNER JOIN courses co ON c.name COLLATE utf8mb4_unicode_ci = co.category COLLATE utf8mb4_unicode_ci
        WHERE co.status = 'ACTIVE'
        ORDER BY c.order
        LIMIT 9
        """, nativeQuery = true)
    List<Category> findTop9CategoriesWithActiveCourses();
}
