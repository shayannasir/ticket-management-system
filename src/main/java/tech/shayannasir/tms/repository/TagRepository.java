package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Tag;

import java.util.UUID;

public interface TagRepository extends CrudRepository<Tag, UUID> {

    Tag findByName(String name);
    Tag findByValue(String value);

}
