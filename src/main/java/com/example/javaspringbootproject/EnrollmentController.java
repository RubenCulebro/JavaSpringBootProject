package com.example.javaspringbootproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller // This means that this class is a Controller
@RequestMapping(path="/enrollment") // This means URL's start with /demo (after Application path)
public class EnrollmentController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private EnrollmentRepository enrollmentRepository;

    @PostMapping(path="/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public @ResponseBody Enrollment addNewEnrollment (@RequestBody Enrollment newEnrollment){
        return enrollmentRepository.save(newEnrollment);
    }

    @GetMapping(path="/list")
    public @ResponseBody Iterable<Enrollment> getAllEnrollments() {
        // This returns a JSON or XML with the users
        return enrollmentRepository.findAll();
    }

    @GetMapping(path="/view/{id}")
    public @ResponseBody Enrollment getEnrollment(@PathVariable Integer id) {
        // This returns a JSON or XML with the enrollment
        return enrollmentRepository.getEnrollmentByEid(id);
    }

    @PutMapping(path="/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Enrollment modifyEnrollment(@RequestBody Enrollment modifiedEnrollment){
        Enrollment enrollment = enrollmentRepository.getEnrollmentByEid(modifiedEnrollment.getEid());

        if (enrollment == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        enrollment.setCourseId(modifiedEnrollment.getCourseId());
        enrollment.setStudentId(modifiedEnrollment.getStudentId());

        return enrollmentRepository.save(enrollment);
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteEnrollment(@RequestParam Integer eid){
        Enrollment enrollment = enrollmentRepository.getEnrollmentByEid(eid);
        if (enrollment == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        enrollmentRepository.delete(enrollment);
        return "Student: " + enrollment.getStudentId() + " was removed from Course: " + enrollment.getCourseId();
    }
}
