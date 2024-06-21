package com.edmitryv.ml_market_server.marketplace.services;

import com.edmitryv.ml_market_server.marketplace.dtos.CategoryDTO;
import com.edmitryv.ml_market_server.marketplace.models.Category;
import com.edmitryv.ml_market_server.marketplace.repos.CategoryRepository;
import com.edmitryv.ml_market_server.marketplace.specifications.CategorySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> search(String text){
        List<CategoryDTO> categoryDTOS = categoryRepository.findAll(CategorySpecifications.search(text)).stream().map(CategoryDTO::new).toList();
        return categoryDTOS;
    }

    public List<Category> getAllCategories(){
      return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }


    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id){
        categoryRepository.deleteById(id);
    }
}
