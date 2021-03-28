package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
