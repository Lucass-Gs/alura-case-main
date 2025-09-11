package br.com.alura.projeto.login;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    public LoginController(CategoryRepository categoryRepository, CourseRepository courseRepository) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categoriesWithCourses = categoryRepository.findTop9CategoriesWithActiveCourses();
        if (categoriesWithCourses == null) {
            categoriesWithCourses = List.of();
        }

        List<Course> allActiveCourses = courseRepository.findAllActiveCourses();
        if (allActiveCourses == null) {
            allActiveCourses = List.of();
        }

        final List<Course> finalAllActiveCourses = allActiveCourses;
        List<CategoryWithCourses> categoriesWithCoursesData = categoriesWithCourses.stream()
                .map(category -> {
                    List<CourseDTO> categoryCourses = finalAllActiveCourses.stream()
                            .filter(course -> course.getCategory().equals(category.getName()))
                            .limit(4)
                            .map(CourseDTO::new)
                            .toList();

                    return new CategoryWithCourses(category, categoryCourses);
                })
                .toList();

        model.addAttribute("categoriesWithCourses", categoriesWithCoursesData);
        model.addAttribute("totalCourses", allActiveCourses.size());

        return "login";
    }

    public record CategoryWithCourses(Category category, List<CourseDTO> courses) { }
}
