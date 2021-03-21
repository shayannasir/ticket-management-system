package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
