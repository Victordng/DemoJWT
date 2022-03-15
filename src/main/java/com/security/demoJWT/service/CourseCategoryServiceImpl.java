package com.security.demoJWT.service;

import com.security.demoJWT.entities.CourseCategory;
import com.security.demoJWT.repositories.CourseCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CourseCategoryServiceImpl implements CourseCategoryService{

    private final CourseCategoryRepository categoryRepository;

    @Override
    public CourseCategory saveCategory(CourseCategory category) {
        return categoryRepository.save(category);
    }

    @Override
    public CourseCategory updateCategory(CourseCategory category, Long id) {

        Optional<CourseCategory> cat = categoryRepository.findById(id);

        if(cat.isPresent()){
            category.setCategoryId(id);
            categoryRepository.save(category);
        }else throw new RuntimeException("Category of course not found!");

        return category;
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<CourseCategory> cat = categoryRepository.findById(id);

        if(cat.isPresent()){
            categoryRepository.deleteById(id);
        }   else throw new RuntimeException("Category of course not found!");
    }

    @Override
    public CourseCategory findById(Long id) {
        Optional<CourseCategory> category = categoryRepository.findById(id);

        if(category.isPresent()){
            return category.get();
        }else throw new RuntimeException("Category of course not found!");

    }

    @Override
    public List<CourseCategory> findAllCategories() {
        return categoryRepository.findAll();
    }
}
