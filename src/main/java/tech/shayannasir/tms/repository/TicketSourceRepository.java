package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.TicketSource;

import java.util.List;

public interface TicketSourceRepository extends CrudRepository<TicketSource, Long> {
    TicketSource findByValue(String value);
    List<TicketSource> findAll();
}
