package pet_project.DiscussHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import pet_project.DiscussHub.dto.Category.CategoryRequest;
import pet_project.DiscussHub.dto.Post.PostRequest;
import pet_project.DiscussHub.dto.Post.PostResponse;
import pet_project.DiscussHub.model.Category;
import pet_project.DiscussHub.model.Post;
import pet_project.DiscussHub.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
  protected CategoryService categoryService;

  @Autowired
  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  public abstract PostResponse postToPostResponse(Post post);

  @Mapping(target = "categories", expression = "java(mapCategories(request.getCategories()))")
  public abstract Post postRequestToPost(PostRequest request, @MappingTarget Post post);

  protected List<Category> mapCategories(List<CategoryRequest> categoryRequests) {
    return categoryRequests.stream()
        .map(c -> categoryService.findCategory(c.getName()))
        .collect(Collectors.toList());
  }
}
