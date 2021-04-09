package tech.shayannasir.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Task;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findAll(Sort sort);
    Page<Task> findAll(Pageable pageable);
    List<Task> findAll();

}
