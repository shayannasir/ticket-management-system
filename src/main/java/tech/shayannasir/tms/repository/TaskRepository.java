package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
