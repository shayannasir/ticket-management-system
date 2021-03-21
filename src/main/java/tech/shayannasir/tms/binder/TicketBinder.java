package tech.shayannasir.tms.binder;

import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.TicketDTO;
import tech.shayannasir.tms.entity.Ticket;

@Component
public class TicketBinder {

    public Ticket bindtoDocument(TicketDTO ticketDTO) {
        return Ticket.builder()
                .contactName(ticketDTO.getContactName())
                .mobile(ticketDTO.getMobile())
                .description(ticketDTO.getDescription())
                .classification(ticketDTO.getClassification())
                .priority(ticketDTO.getPriority())
                .status(ticketDTO.getStatus())
                .email(ticketDTO.getEmail())
                .subject(ticketDTO.getSubject())
                .workID(ticketDTO.getWorkID())
                .tags(ticketDTO.getTags())
                .build();
    }
}
