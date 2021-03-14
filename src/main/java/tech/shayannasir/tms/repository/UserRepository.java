package tech.shayannasir.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    User findByUsername(String username);

    List<User> findAll(Sort sort);

    Page<User> findAll(Pageable pageable);

    List<User> findAll();

}
