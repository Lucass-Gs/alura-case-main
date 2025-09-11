package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
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

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseController(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/courses")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> coursePage = courseRepository.findAll(pageable);

        List<Map<String, Object>> items = coursePage.getContent().stream()
                .map(course -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", course.getId());
                    item.put("name", course.getName());
                    item.put("code", course.getCode());
                    item.put("instructor", course.getInstructor());
                    item.put("category", course.getCategory());
                    item.put("description", course.getDescription());
                    item.put("status", course.getStatus().toString());
                    item.put("createdAt", Date.from(course.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                    item.put("inactivationDate", course.getInactivationDate() != null ? 
                        Date.from(course.getInactivationDate().atZone(ZoneId.systemDefault()).toInstant()) : null);
                    return item;
                })
                .toList();

        List<Map<String, Object>> columns = new ArrayList<>();
        columns.add(createColumn("name", "Nome", "text"));
        columns.add(createColumn("code", "Código", "text"));
        columns.add(createColumn("instructor", "Instrutor", "text"));
        columns.add(createColumn("category", "Categoria", "text"));
        columns.add(createColumn("status", "Status", "status"));
        columns.add(createColumn("createdAt", "Criado em", "date"));
        columns.add(createColumn("inactivationDate", "Desativado em", "date"));

        List<Map<String, Object>> actions = new ArrayList<>();
        actions.add(createAction("edit", "Editar", "/admin/course/edit/", "id"));

        int totalPages = coursePage.getTotalPages();
        int currentPage = page;
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        model.addAttribute("datagridTitle", "Cursos");
        model.addAttribute("datagridNewUrl", "/admin/course/new");
        model.addAttribute("datagridNewText", "Novo Curso");
        model.addAttribute("datagridItems", items);
        model.addAttribute("datagridColumns", columns);
        model.addAttribute("datagridActions", actions);
        model.addAttribute("datagridCurrentPage", currentPage);
        model.addAttribute("datagridTotalPages", totalPages);
        model.addAttribute("datagridStartPage", startPage);
        model.addAttribute("datagridEndPage", endPage);
        model.addAttribute("datagridItemsPerPage", size);
        model.addAttribute("datagridTotalItems", coursePage.getTotalElements());
        model.addAttribute("datagridStartItem", page * size + 1);
        model.addAttribute("datagridEndItem", Math.min((long) (page + 1) * size, coursePage.getTotalElements()));

        return "admin/course/list";
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
    

    @GetMapping("/admin/course/new")
    public String create(NewCourseForm newCourse, Model model) {
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .sorted((c1, c2) -> Integer.compare(c1.getOrder(), c2.getOrder()))
                .toList();
        
        model.addAttribute("categories", categories);
        return "admin/course/newForm";
    }

    @Transactional
    @PostMapping("/admin/course/new")
    public String save(@Valid NewCourseForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll()
                    .stream()
                    .sorted((c1, c2) -> Integer.compare(c1.getOrder(), c2.getOrder()))
                    .toList();
            
            model.addAttribute("categories", categories);
            return "admin/course/newForm";
        }

        if (courseRepository.existsByCode(form.getCode())) {
            return create(form, model);
        }

        courseRepository.save(form.toModel());
        return "redirect:/admin/courses";
    }


    @GetMapping("/admin/course/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        
        if (courseOpt.isEmpty()) {
            return "redirect:/admin/courses";
        }
        
        Course course = courseOpt.get();
        EditCourseForm editForm = new EditCourseForm(course);
        
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .sorted((c1, c2) -> Integer.compare(c1.getOrder(), c2.getOrder()))
                .toList();
        
        model.addAttribute("editCourseForm", editForm);
        model.addAttribute("courseId", id);
        model.addAttribute("categories", categories);
        
        return "admin/course/editForm";
    }

    @Transactional
    @PostMapping("/admin/course/edit/{id}")
    public String update(@PathVariable Long id, @Valid EditCourseForm form, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll()
                    .stream()
                    .sorted((c1, c2) -> Integer.compare(c1.getOrder(), c2.getOrder()))
                    .toList();
            
            model.addAttribute("categories", categories);
            model.addAttribute("courseId", id);
            return "admin/course/editForm";
        }
        
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            return "redirect:/admin/courses";
        }
        
        Course course = courseOpt.get();

        if (!course.getCode().equals(form.getCode()) && courseRepository.existsByCode(form.getCode())) {
            result.rejectValue("code", "error.code", "Código já existe");
            List<Category> categories = categoryRepository.findAll()
                    .stream()
                    .sorted((c1, c2) -> Integer.compare(c1.getOrder(), c2.getOrder()))
                    .toList();
            
            model.addAttribute("categories", categories);
            model.addAttribute("courseId", id);
            return "admin/course/editForm";
        }
        
        form.updateCourse(course);
        courseRepository.save(course);
        
        return "redirect:/admin/courses";
    }

}
