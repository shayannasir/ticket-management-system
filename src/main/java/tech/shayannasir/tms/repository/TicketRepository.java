package tech.shayannasir.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import tech.shayannasir.tms.entity.Ticket;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    List<Ticket> findAll(Sort sort);
    Page<Ticket> findAll(Pageable pageable);
    List<Ticket> findAll();
}
