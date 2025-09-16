package br.com.alura.projeto.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() {

        category1 = new Category("Programação", "prog", "#00C86F", 1);
        entityManager.persistAndFlush(category1);

        category2 = new Category("Frontend", "frontend", "#6BD1FF", 2);
        entityManager.persistAndFlush(category2);

        category3 = new Category("Backend", "backend", "#FF6B6B", 3);
        entityManager.persistAndFlush(category3);
    }

    @Test
    @DisplayName("should find all categories ordered by order")
    void shouldFindAllCategoriesOrderedByOrder() {
        List<Category> categories = categoryRepository.findAllOrderedByOrder();

        assertThat(categories).hasSize(3);
        assertThat(categories).extracting(Category::getCode)
                .containsExactly("prog", "frontend", "backend");
        assertThat(categories).extracting(Category::getOrder)
                .containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("should return true when category code exists")
    void shouldReturnTrueWhenCategoryCodeExists() {
        boolean exists = categoryRepository.existsByCode("prog");


        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should return false when category code does not exist")
    void shouldReturnFalseWhenCategoryCodeDoesNotExist() {
        boolean exists = categoryRepository.existsByCode("nonexistent");


        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("should find top 9 categories with active courses")
    void shouldFindTop9CategoriesWithActiveCourses() {
        List<Category> categories = categoryRepository.findTop9CategoriesWithActiveCourses();


        assertThat(categories).isNotNull();

    }

    @Test
    @DisplayName("should save new category")
    void shouldSaveNewCategory() {
        Category newCategory = new Category("Mobile", "mobile", "#9CD33B", 4);

        Category savedCategory = categoryRepository.save(newCategory);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getCode()).isEqualTo("mobile");
        assertThat(categoryRepository.existsByCode("mobile")).isTrue();
    }

    @Test
    @DisplayName("should update existing category")
    void shouldUpdateExistingCategory() {

        Category category = categoryRepository.findAll().get(0);
        category.setName("Programação Avançada");

        Category updatedCategory = categoryRepository.save(category);

        assertThat(updatedCategory.getName()).isEqualTo("Programação Avançada");
        assertThat(updatedCategory.getCode()).isEqualTo(category.getCode());
    }

    @Test
    @DisplayName("should delete category")
    void shouldDeleteCategory() {

        Category category = categoryRepository.findAll().get(0);

        categoryRepository.delete(category);

        assertThat(categoryRepository.existsByCode(category.getCode())).isFalse();
        assertThat(categoryRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("should find category by id")
    void shouldFindCategoryById() {
        Category category = categoryRepository.findAll().get(0);

        var foundCategory = categoryRepository.findById(category.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCode()).isEqualTo(category.getCode());
    }

    @Test
    @DisplayName("should find all categories")
    void shouldFindAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        assertThat(allCategories).hasSize(3);
        assertThat(allCategories).extracting(Category::getCode)
                .containsExactlyInAnyOrder("prog", "frontend", "backend");
    }

    @Test
    @DisplayName("should count categories")
    void shouldCountCategories() {
        long count = categoryRepository.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("should check if category exists by id")
    void shouldCheckIfCategoryExistsById() {
        Category category = categoryRepository.findAll().get(0);

        boolean exists = categoryRepository.existsById(category.getId());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should check if category does not exist by id")
    void shouldCheckIfCategoryDoesNotExistById() {
        boolean exists = categoryRepository.existsById(999L);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("should find categories with specific order range")
    void shouldFindCategoriesWithSpecificOrderRange() {
        Category category4 = new Category("DevOps", "devops", "#FF8C00", 4);
        entityManager.persistAndFlush(category4);

        List<Category> categories = categoryRepository.findAllOrderedByOrder();

        assertThat(categories).hasSize(4);
        assertThat(categories).extracting(Category::getOrder)
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    @DisplayName("should handle empty repository")
    void shouldHandleEmptyRepository() {
        categoryRepository.deleteAll();

        List<Category> categories = categoryRepository.findAllOrderedByOrder();
        long count = categoryRepository.count();

        assertThat(categories).isEmpty();
        assertThat(count).isEqualTo(0);
    }
}

