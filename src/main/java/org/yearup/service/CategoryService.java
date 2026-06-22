package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
       return categoryRepository.findAll();
    }

    public Category getById(int categoryId)
    {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category create(Category category)
    {
        return categoryRepository.save(category);
    }

    public Category update(int categoryId, Category category)
    {
        Category update = getById(categoryId);

        update.setDescription(category.getDescription());
        update.setName(category.getName());

        return categoryRepository.save(update);
    }

    public void delete(int categoryId)
    {
        categoryRepository.delete(getById(categoryId));
    }

    public List<Category> search(String name, String description){
        List<Category> results = new ArrayList<>(categoryRepository.findAll().stream().filter(c -> name == null || c.getName().contains(name))
                .filter(c -> description == null || c.getDescription().contains(description)).toList());

        return results;
    }
}
