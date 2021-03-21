package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.TicketPriority;

public interface TicketPriorityRepository extends CrudRepository<TicketPriority, Long> {
    TicketPriority findByName(String name);
    TicketPriority findByValue(String value);
}
