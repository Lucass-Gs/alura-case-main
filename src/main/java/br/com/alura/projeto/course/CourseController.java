package br.com.alura.projeto.course;

import br.com.alura.projeto.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/admin/courses")
    public String list(Model model) {
        List<CourseDTO> list = courseRepository.findAll()
                .stream()
                .map(CourseDTO::new)
                .toList();

        model.addAttribute("courses", list);

        return "admin/course/list";
    }

    @GetMapping("/admin/course/new")
    public String create(NewCourseForm newCourse, Model model) { return "admin/course/newForm"; }

    @Transactional
    @PostMapping("/admin/course/new")
    public String save(@Valid NewCourseForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return create(form, model);
        }

        if (courseRepository.existsByCode(form.getCode())) {
            return create(form, model);
        }

        courseRepository.save(form.toModel());
        return "redirect:/admin/courses";
    }

    @Transactional
    @PostMapping("/course/{code}/inactive")
    @ResponseBody
    public ResponseEntity<?> inactivateCourse(@PathVariable String code) {
        Optional<Course> courseOpt = courseRepository.findByCode(code);
        
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorItemDTO("code", "Curso não encontrado"));
        }
        
        Course course = courseOpt.get();
        
        if (course.getStatus() == CourseStatus.INACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("status", "Curso já está inativo"));
        }
        
        course.inactivate();
        courseRepository.save(course);
        
        return ResponseEntity.ok().build();
    }
}
