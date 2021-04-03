package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Department;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    Department findByValue(String value);
    List<Department> findAll();
}
