package com.security.demoJWT.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.demoJWT.entities.Course;
import com.security.demoJWT.entities.CourseCategory;
import com.security.demoJWT.entities.User;
import com.security.demoJWT.service.CourseCategoryService;
import com.security.demoJWT.service.CourseService;
import com.security.demoJWT.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class DemoJWTController {

    private final UserService userService;
    private final CourseService courseService;
    private final CourseCategoryService categoryService;


    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> findAllUsers(){
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findOne(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateOne(@PathVariable("id") Long id,@RequestBody User user ){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}").toUriString());
        return ResponseEntity.created(uri).body(userService.updateUser(user,id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/courses",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Course>> findAllCourses(){
        return ResponseEntity.ok().body(courseService.findAllCourses());
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> findCourse(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(courseService.findById(id));
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> saveCourse(@RequestBody Course course){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/courses").toUriString());
        return ResponseEntity.created(uri).body(courseService.saveCourse(course));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable("id") Long id,@RequestBody Course course ){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/courses/{id}").toUriString());
        return ResponseEntity.created(uri).body(courseService.updateCourse(course,id));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/categories",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<CourseCategory> saveCategory(@RequestBody CourseCategory category) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories").toUriString());
        return ResponseEntity.created(uri).body(categoryService.saveCategory(category));
    }

    @PutMapping(value = "/categories/{id}",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<CourseCategory> updateCategory(@RequestBody CourseCategory category, @PathVariable("id") Long id) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories").toUriString());
        return ResponseEntity.created(uri).body(categoryService.updateCategory(category,id));
    }

    @DeleteMapping(value = "/categories/{id}",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/categories/{id}",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<CourseCategory> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }

    @GetMapping(value = "/categories",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<List<CourseCategory>> findCategories() {
        return ResponseEntity.ok().body(categoryService.findAllCategories());
    }

    @PostMapping(value = "/addcourse",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addCourseToUser(@RequestBody AddCourseUserForm form){
        userService.addCourseToUser(form.getCourseId(), form.getMatricule());
        return ResponseEntity.ok().build();
    }
    @PostMapping(value = "/deletecourse",produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<?> removeCourseToUser(@RequestBody AddCourseUserForm form){
        userService.deleteCourseToUser(form.getCourseId(), form.getMatricule());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader.startsWith("Bearer ")) {
            String refresh_token = authorizationHeader.substring("Bearer ".length());

            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);

            String username = decodedJWT.getSubject();
            List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("roles").asList(String.class)
                    .stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

            List<String> roles = new ArrayList<>();
            authorities.stream().forEach(auth -> roles.add(auth.getAuthority()));

            String access_token = JWT.create()
                    .withSubject(username)
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", roles)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .sign(algorithm);

            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String , String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token",refresh_token);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        }
    }

}

@Data
class AddCourseUserForm{
    private Long courseId;
    private String matricule;
}
