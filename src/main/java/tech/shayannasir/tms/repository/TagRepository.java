package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Tag;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByName(String name);
    Tag findByValue(String value);
    List<Tag> findAll();

}
