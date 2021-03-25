package tech.shayannasir.tms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.Tag;
import tech.shayannasir.tms.entity.TicketClassification;
import tech.shayannasir.tms.entity.TicketPriority;
import tech.shayannasir.tms.entity.TicketStatus;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    Long id;
    String contactName;
    String mobile;
    String email;
    String workID;
    String subject;

    TicketStatus status;

    TicketPriority priority;

    TicketClassification classification;

    String description;

    List<Tag> tags;
}
