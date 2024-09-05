package pet_project.DiscussHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pet_project.DiscussHub.dto.Category.CategoryRequest;
import pet_project.DiscussHub.dto.Category.CategoryResponse;
import pet_project.DiscussHub.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryResponse categoryToCategoryResponse(Category category);

  Category categoryRequestToCategory(CategoryRequest request, @MappingTarget Category category);
}
