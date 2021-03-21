package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByName(String name);
    Tag findByValue(String value);

}
