package tech.shayannasir.tms.binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.ArticleResponseDTO;
import tech.shayannasir.tms.entity.Article;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.repository.UserRepository;

import java.util.Optional;

@Component
public class ArticleBinder {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDataBinder userDataBinder;

    public ArticleResponseDTO bindDocumentToDTO(Article source) {
        ArticleResponseDTO target = new ArticleResponseDTO();

        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setStatus(source.getStatus());
        target.setTags(source.getTags());
        target.setComments(source.getComments());
        target.setLikes(source.getLikes());
        target.setDislikes(source.getDislikes());
        target.setViews(source.getViews());
        target.setCreatedDate(source.getCreatedDate());
        target.setLastModifiedDate(source.getLastModifiedDate());

        Optional<User> createdBy = userRepository.findById(source.getCreatedBy());
        createdBy.ifPresent(user -> target.setCreatedBy(userDataBinder.bindDocumentToDetailDTO(Optional.of(user).orElse(null))));

        Optional<User> modifiedBy = userRepository.findById(source.getLastModifiedBy());
        modifiedBy.ifPresent(user -> target.setLastModifiedBy(userDataBinder.bindDocumentToDetailDTO(Optional.of(user).orElse(null))));

        return target;
    }

}
