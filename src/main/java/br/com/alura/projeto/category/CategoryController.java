package br.com.alura.projeto.category;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/categories")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("order").ascending());
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Map<String, Object>> items = categoryPage.getContent().stream()
                .map(category -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", category.getId());
                    item.put("name", category.getName());
                    item.put("code", category.getCode());
                    item.put("color", category.getColor());
                    item.put("order", category.getOrder());
                    return item;
                })
                .toList();

        List<Map<String, Object>> columns = new ArrayList<>();
        columns.add(createColumn("name", "Nome", "text"));
        columns.add(createColumn("code", "Código", "text"));
        columns.add(createColumn("color", "Cor", "text"));
        columns.add(createColumn("order", "Ordem", "number"));

        List<Map<String, Object>> actions = new ArrayList<>();
        actions.add(createAction("edit", "Editar", "/admin/category/edit/", "id"));

        int totalPages = categoryPage.getTotalPages();
        int currentPage = page;
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        model.addAttribute("datagridTitle", "Categorias");
        model.addAttribute("datagridNewUrl", "/admin/category/new");
        model.addAttribute("datagridNewText", "Nova Categoria");
        model.addAttribute("datagridItems", items);
        model.addAttribute("datagridColumns", columns);
        model.addAttribute("datagridActions", actions);
        model.addAttribute("datagridCurrentPage", currentPage);
        model.addAttribute("datagridTotalPages", totalPages);
        model.addAttribute("datagridStartPage", startPage);
        model.addAttribute("datagridEndPage", endPage);
        model.addAttribute("datagridItemsPerPage", size);
        model.addAttribute("datagridTotalItems", categoryPage.getTotalElements());
        model.addAttribute("datagridStartItem", page * size + 1);
        model.addAttribute("datagridEndItem", Math.min((long) (page + 1) * size, categoryPage.getTotalElements()));

        return "admin/category/list";
    }
    
    private Map<String, Object> createColumn(String field, String label, String type) {
        Map<String, Object> column = new HashMap<>();
        column.put("field", field);
        column.put("label", label);
        column.put("type", type);
        return column;
    }
    
    private Map<String, Object> createAction(String type, String label, String url, String idField) {
        Map<String, Object> action = new HashMap<>();
        action.put("type", type);
        action.put("label", label);
        action.put("url", url);
        action.put("idField", idField);
        return action;
    }

    @GetMapping("/admin/category/new")
    public String create(NewCategoryForm newCategory, Model model) {
        return "admin/category/newForm";
    }

    @Transactional
    @PostMapping("/admin/category/new")
    public String save(@Valid NewCategoryForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return create(form, model);
        }

        if (categoryRepository.existsByCode(form.getCode())) {
            return create(form, model);
        }

        categoryRepository.save(form.toModel());
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        
        if (categoryOpt.isEmpty()) {
            return "redirect:/admin/categories";
        }
        
        Category category = categoryOpt.get();
        EditCategoryForm editForm = new EditCategoryForm(category);
        
        model.addAttribute("editCategoryForm", editForm);
        model.addAttribute("categoryId", id);
        
        return "admin/category/editForm";
    }

    @Transactional
    @PostMapping("/admin/category/edit/{id}")
    public String update(@PathVariable Long id, @Valid EditCategoryForm form, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "admin/category/editForm";
        }
        
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return "redirect:/admin/categories";
        }
        
        Category category = categoryOpt.get();

        if (!category.getCode().equals(form.getCode()) && categoryRepository.existsByCode(form.getCode())) {
            result.rejectValue("code", "error.code", "Código já existe");
            model.addAttribute("categoryId", id);
            return "admin/category/editForm";
        }
        
        form.updateCategory(category);
        categoryRepository.save(category);
        
        return "redirect:/admin/categories";
    }

}
