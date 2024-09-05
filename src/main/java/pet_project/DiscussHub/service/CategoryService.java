package pet_project.DiscussHub.service;

import java.util.List;
import java.util.UUID;
import pet_project.DiscussHub.dto.Category.CategoryRequest;
import pet_project.DiscussHub.dto.Category.CategoryResponse;
import pet_project.DiscussHub.model.Category;

public interface CategoryService {
  Category findCategory(String name);

  CategoryResponse create(CategoryRequest request);

  List<CategoryResponse> readAll();

  CategoryResponse readById(UUID id);

  CategoryResponse readByName(String name);

  CategoryResponse update(UUID id, CategoryRequest request);

  void delete(UUID id);
}
