package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.InvalidJwt;

public interface InvalidJwtRepository extends CrudRepository<InvalidJwt, Long> {

    boolean existsByToken(String token);
}
