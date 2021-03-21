package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.TicketStatus;

public interface TicketStatusRepository extends CrudRepository<TicketStatus, Long> {
    TicketStatus findByName(String name);
    TicketStatus findByValue(String value);
}
