package tech.shayannasir.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.EndUser;

import java.util.List;

public interface EndUserRepository extends CrudRepository<EndUser, Long> {
    EndUser findByEmail(String email);
    EndUser findByWorkID(String workID);
    List<EndUser> findAll(Sort sort);
    Page<EndUser> findAll(Pageable pageable);
    List<EndUser> findAll();
}
