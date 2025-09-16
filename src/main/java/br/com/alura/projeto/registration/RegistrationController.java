package br.com.alura.projeto.registration;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/registration/new")
    public ResponseEntity<?> createRegistration(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        Optional<User> userOpt = userRepository.findByEmail(newRegistration.getStudentEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado com o email: " + newRegistration.getStudentEmail());
        }

        Optional<Course> courseOpt = courseRepository.findByCode(newRegistration.getCourseCode());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso não encontrado: " + newRegistration.getCourseCode());
        }

        User user = userOpt.get();
        Course course = courseOpt.get();

        if (course.getStatus() != CourseStatus.ACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não pode se registrar no curso inativo: " + course.getCode());
        }

        if (registrationRepository.existsByUserAndCourse(user, course)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("O usuário já está registrado nesse curso");
        }

        Registration registration = new Registration(user, course);
        registrationRepository.save(registration);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<Object[]> results = registrationRepository.findCourseRegistrationReport();
        
        List<RegistrationReportItem> items = results.stream()
                .map(row -> new RegistrationReportItem(
                        (String) row[0], // courseName
                        (String) row[1], // courseCode
                        (String) row[2], // instructorName
                        (String) row[3], // instructorEmail
                        ((Number) row[4]).longValue() // totalRegistrations
                ))
                .toList();

        return ResponseEntity.ok(items);
    }

}
