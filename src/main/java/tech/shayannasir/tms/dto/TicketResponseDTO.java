package tech.shayannasir.tms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.*;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    Long id;

    String subject;

    EndUser endUser;

    TicketStatus status;

    TicketPriority priority;

    TicketClassification classification;

    TicketSource source;

    String description;

    List<Tag> tags;

    UserDetailDTO assignedTo;

    List<Comment> comments;

}
