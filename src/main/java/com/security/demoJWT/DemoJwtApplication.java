package com.security.demoJWT;

import com.security.demoJWT.entities.Course;
import com.security.demoJWT.entities.CourseCategory;
import com.security.demoJWT.entities.User;
import com.security.demoJWT.enums.UserType.UserType;
import com.security.demoJWT.repositories.CourseCategoryRepository;
import com.security.demoJWT.service.CourseCategoryService;
import com.security.demoJWT.service.CourseService;
import com.security.demoJWT.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


	@Bean
	CommandLineRunner run (UserService userService,
						   CourseService courseService,
						   CourseCategoryService categoryService
	){
		return args -> {
			User user= new User();
			user.setUsername("victor");
			user.setPassword("12345");
			user.setUserType(UserType.STUDENT);
			user.setMatricule("ST1230");
			userService.saveUser(user);

			User user2= new User();
			user2.setUsername("marie");
			user2.setPassword("12345");
			user2.setUserType(UserType.PROFESSOR);
			user2.setMatricule("PR1245");
			userService.saveUser(user2);

			User user3= new User();
			user3.setUsername("edy");
			user3.setPassword("12345");
			user3.setUserType(UserType.STUDENT);
			user3.setMatricule("PR2904");
			userService.saveUser(user3);


			CourseCategory category = new CourseCategory();
			category.setCategoryName("SCIENCES");
			categoryService.saveCategory(category);

			CourseCategory category2 = new CourseCategory();
			category2.setCategoryName("INFORMATION TECHNOLOGY");
			categoryService.saveCategory(category2);

			CourseCategory category3 = new CourseCategory();
			category3.setCategoryName("LANGUAGES");
			categoryService.saveCategory(category3);


			Course course = new Course();
			course.setCourseName("Mathematic");
			course.setCategory(category);
			courseService.saveCourse(course);

			Course course2 = new Course();
			course2.setCourseName("Data science");
			course2.setCategory(category2);
			courseService.saveCourse(course2);

			Course course3 = new Course();
			course3.setCourseName("English");
			course3.setCategory(category3);
			courseService.saveCourse(course3);

			Course course4 = new Course();
			course4.setCourseName("Physic");
			course4.setCategory(category);
			courseService.saveCourse(course4);

			Course course5 = new Course();
			course5.setCourseName("Software engineering");
			course5.setCategory(category2);
			courseService.saveCourse(course5);
		};
	}

}
