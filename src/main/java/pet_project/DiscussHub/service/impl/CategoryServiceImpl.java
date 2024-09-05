package pet_project.DiscussHub.service.impl;

import static pet_project.DiscussHub.constant.ErrorConstants.ENTITY_NOT_FOUND_MESSAGE;
import static pet_project.DiscussHub.utils.ValidationUtils.validateRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Category.CategoryRequest;
import pet_project.DiscussHub.dto.Category.CategoryResponse;
import pet_project.DiscussHub.mapper.CategoryMapper;
import pet_project.DiscussHub.model.Category;
import pet_project.DiscussHub.repository.CategoryRepository;
import pet_project.DiscussHub.service.CategoryService;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  private Category findCategoryById(UUID id) {
    return this.categoryRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, id)));
  }

  @Override
  public Category findCategory(String name) {
    return this.categoryRepository
        .findByName(name)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, name)));
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
