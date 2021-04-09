package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Activity;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
