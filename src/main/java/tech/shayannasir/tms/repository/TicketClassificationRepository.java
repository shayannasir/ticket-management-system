package tech.shayannasir.tms.repository;

import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.TicketClassification;

import java.util.List;


public interface TicketClassificationRepository extends CrudRepository<TicketClassification, Long> {
    TicketClassification findByName(String name);
    TicketClassification findByValue(String value);
    List<TicketClassification> findAll();
}
