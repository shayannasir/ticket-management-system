package tech.shayannasir.tms.binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.TicketResponseDTO;
import tech.shayannasir.tms.entity.EndUser;
import tech.shayannasir.tms.entity.Ticket;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.repository.EndUserRepository;
import tech.shayannasir.tms.repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component
public class TicketBinder {

    @Autowired
    private EndUserRepository endUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDataBinder userDataBinder;

    public Ticket bindtoDocument(TicketResponseDTO ticketDTO) {
        return Ticket.builder()
                .description(ticketDTO.getDescription())
                .classification(ticketDTO.getClassification())
                .priority(ticketDTO.getPriority())
                .status(ticketDTO.getStatus())
                .subject(ticketDTO.getSubject())
                .tags(ticketDTO.getTags())
                .build();
    }

    public TicketResponseDTO bindToDTO(Ticket source) {
        TicketResponseDTO target = new TicketResponseDTO();

        target.setId(source.getId());
        Optional<EndUser> optionalEndUser = endUserRepository.findById(source.getEndUserID());
        optionalEndUser.ifPresent(endUser -> target.setEndUser(Optional.of(endUser).orElse(null)));
        Optional<User> optionalUser = userRepository.findById(source.getAssignedToID());
        optionalUser.ifPresent(user -> target.setAssignedTo(userDataBinder.bindDocumentToDetailDTO(Optional.of(user).orElse(null))));
        target.setSubject(source.getSubject());
        target.setStatus(source.getStatus());
        target.setPriority(source.getPriority());
        target.setClassification(source.getClassification());
        target.setSource(source.getTicketSource());
        target.setDescription(source.getDescription());
        target.setTags(source.getTags());
        target.setComments(source.getComments());

        return target;
    }

}
