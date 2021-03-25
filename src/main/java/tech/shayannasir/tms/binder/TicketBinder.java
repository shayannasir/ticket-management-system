package tech.shayannasir.tms.binder;

import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.TicketResponseDTO;
import tech.shayannasir.tms.entity.Ticket;

@Component
public class TicketBinder {

    public Ticket bindtoDocument(TicketResponseDTO ticketDTO) {
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

    public TicketResponseDTO bindToDTO(Ticket source) {
        TicketResponseDTO target = new TicketResponseDTO();

        target.setId(source.getId());
        target.setContactName(source.getContactName());
        target.setMobile(source.getMobile());
        target.setEmail(source.getEmail());
        target.setWorkID(source.getWorkID());
        target.setSubject(source.getSubject());
        target.setStatus(source.getStatus());
        target.setPriority(source.getPriority());
        target.setClassification(source.getClassification());
        target.setDescription(source.getDescription());
        target.setTags(source.getTags());

        return target;
    }
}
