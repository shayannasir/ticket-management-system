package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {
}
