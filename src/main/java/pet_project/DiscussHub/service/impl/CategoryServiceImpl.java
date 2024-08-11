package pet_project.DiscussHub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Category.CategoryRequest;
import pet_project.DiscussHub.dto.Category.CategoryResponse;
import pet_project.DiscussHub.mapper.CategoryMapper;
import pet_project.DiscussHub.model.Category;
import pet_project.DiscussHub.repository.CategoryRepository;
import pet_project.DiscussHub.service.CategoryService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static pet_project.DiscussHub.utils.ValidationUtils.validateRequest;

@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  private Category findCategoryById(UUID id) {
    return this.categoryRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("Category with such id = " + id + " was not found"));
  }

  @Override
  public Category findCategory(String name) {
    return this.categoryRepository
        .findByName(name)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Category with such name = " + name + " was not found"));
  }

  @Override
  public CategoryResponse create(CategoryRequest request) {
    validateRequest(request);

    Category category = this.categoryMapper.categoryRequestToCategory(request, new Category());
    return this.categoryMapper.categoryToCategoryResponse(this.categoryRepository.save(category));
  }

  @Override
  public List<CategoryResponse> readAll() {
    List<Category> categories = this.categoryRepository.findAll();
    return categories.stream()
        .map(this.categoryMapper::categoryToCategoryResponse)
        .collect(Collectors.toList());
  }

  @Override
  public CategoryResponse readById(UUID id) {
    Category category = this.findCategoryById(id);
    return this.categoryMapper.categoryToCategoryResponse(category);
  }

  @Override
  public CategoryResponse readByName(String name) {
    Category category = this.findCategory(name);
    return this.categoryMapper.categoryToCategoryResponse(category);
  }

  @Override
  public CategoryResponse update(UUID id, CategoryRequest request) {
    validateRequest(request);

    Category toUpdate = this.findCategoryById(id);
    Category category = this.categoryMapper.categoryRequestToCategory(request, toUpdate);
    return this.categoryMapper.categoryToCategoryResponse(this.categoryRepository.save(category));
  }

  @Override
  public void delete(UUID id) {
    this.categoryRepository.delete(this.findCategoryById(id));
  }
}
