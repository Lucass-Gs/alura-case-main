package br.com.alura.projeto.category;

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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category category;
    private NewCategoryForm validForm;

    @BeforeEach
    void setUp() {
        category = new Category("Programação", "prog", "#FF6B6B", 1);

        try {
            java.lang.reflect.Field idField = Category.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(category, 1L);
        } catch (Exception e) {

        }

        validForm = new NewCategoryForm();
        validForm.setName("Programação");
        validForm.setCode("prog");
        validForm.setColor("#FF6B6B");
        validForm.setOrder(1);
    }

    @Test
    void shouldReturnCategoryListPage() throws Exception {
        List<Category> categories = Collections.singletonList(category);
        Page<Category> categoryPage = new PageImpl<>(categories);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/list"))
                .andExpect(model().attributeExists("datagridItems"));

        verify(categoryRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnNewCategoryFormPage() throws Exception {
        mockMvc.perform(get("/admin/category/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));
    }

    @Test
    void shouldSaveCategoryWithValidData() throws Exception {
        when(categoryRepository.existsByCode(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("color", validForm.getColor())
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryRepository).existsByCode(validForm.getCode());
        verify(categoryRepository).save(any(Category.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldReturnFormWithValidationErrorsWhenNameIsBlank(String name) throws Exception {
        mockMvc.perform(post("/admin/category/new")
                .param("name", name)
                .param("code", validForm.getCode())
                .param("color", validForm.getColor())
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldReturnFormWithValidationErrorsWhenCodeIsBlank(String code) throws Exception {
        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", code)
                .param("color", validForm.getColor())
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldReturnFormWithValidationErrorsWhenColorIsBlank(String color) throws Exception {
        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("color", color)
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void shouldReturnFormWithValidationErrorsWhenOrderIsInvalid(int order) throws Exception {
        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("color", validForm.getColor())
                .param("order", String.valueOf(order))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "very-long-code", "123"})
    void shouldReturnFormWithValidationErrorsWhenCodeIsInvalid(String invalidCode) throws Exception {
        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", invalidCode)
                .param("color", validForm.getColor())
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldReturnFormWhenCategoryCodeAlreadyExists() throws Exception {
        when(categoryRepository.existsByCode(validForm.getCode())).thenReturn(true);

        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", validForm.getCode())
                .param("color", validForm.getColor())
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/newForm"));

        verify(categoryRepository).existsByCode(validForm.getCode());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"prog", "front-end"})
    void shouldAcceptValidCodes(String validCode) throws Exception {
        when(categoryRepository.existsByCode(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/admin/category/new")
                .param("name", validForm.getName())
                .param("code", validCode)
                .param("color", validForm.getColor())
                .param("order", String.valueOf(validForm.getOrder()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryRepository).existsByCode(validCode);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldReturnEditCategoryFormPage() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/admin/category/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/editForm"))
                .andExpect(model().attributeExists("editCategoryForm"))
                .andExpect(model().attributeExists("categoryId"));

        verify(categoryRepository).findById(1L);
    }

    @Test
    void shouldRedirectToCategoriesListWhenCategoryNotFoundForEdit() throws Exception {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/category/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryRepository).findById(999L);
    }

    @Test
    void shouldUpdateCategoryWithValidData() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "Programação Updated")
                .param("code", "prog")
                .param("color", "#00FF00")
                .param("order", "2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryRepository).findById(1L);

        verify(categoryRepository, never()).existsByCode("prog");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldReturnFormWithValidationErrorsWhenEditingWithInvalidData() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "")
                .param("code", "prog")
                .param("color", "#FF6B6B")
                .param("order", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/editForm"));

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldReturnFormWhenCategoryCodeAlreadyExistsInAnotherCategory() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCode("mobile")).thenReturn(true);

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "Programação")
                .param("code", "mobile")
                .param("color", "#FF6B6B")
                .param("order", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/editForm"));

        verify(categoryRepository).existsByCode("mobile");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldAllowSameCodeForSameCategory() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCode("prog")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "Programação")
                .param("code", "prog")
                .param("color", "#FF6B6B")
                .param("order", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldUpdateCategoryWithNewValues() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCode("prog")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            return savedCategory;
        });

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "Programação Avançada")
                .param("code", "prog")
                .param("color", "#00FF00")
                .param("order", "5")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryRepository).save(argThat(category -> 
            "Programação Avançada".equals(category.getName()) &&
            "#00FF00".equals(category.getColor()) &&
            category.getOrder() == 5
        ));
    }
}
