package com.security.demoJWT.service;

import com.security.demoJWT.entities.CourseCategory;

import java.util.List;


public interface CourseCategoryService {

    CourseCategory saveCategory(CourseCategory category);
    CourseCategory updateCategory(CourseCategory category, Long id);
    void deleteCategory(Long id);
    CourseCategory findById(Long id);
    List<CourseCategory> findAllCategories();

}
