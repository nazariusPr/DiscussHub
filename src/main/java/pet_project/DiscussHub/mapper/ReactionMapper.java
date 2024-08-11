package pet_project.DiscussHub.mapper;

import org.mapstruct.Mapper;
import pet_project.DiscussHub.dto.Reaction.ReactionRequest;
import pet_project.DiscussHub.model.Reaction;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
  Reaction reactionRequestToReaction(ReactionRequest reactionRequest);
}
