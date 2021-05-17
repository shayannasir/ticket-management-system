package tech.shayannasir.tms.binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.ArticleResponseDTO;
import tech.shayannasir.tms.dto.CreatedModifiedUserDTO;
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
        CreatedModifiedUserDTO createdModifiedUserDTO = userDataBinder.fetchCreatedAndModifiedUsersFor(source.getCreatedBy(), source.getLastModifiedBy());

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
        target.setCreatedBy(createdModifiedUserDTO.getCreatedBy());
        target.setLastModifiedBy(createdModifiedUserDTO.getModifiedBy());
        target.setCoverPic(source.getCoverPic());
        target.setAttachments(source.getAttachments());

        return target;
    }

}
