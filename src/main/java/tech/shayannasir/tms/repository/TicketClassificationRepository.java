package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.TicketClassification;


public interface TicketClassificationRepository extends CrudRepository<TicketClassification, Long> {
    TicketClassification findByName(String name);
    TicketClassification findByValue(String value);
}
