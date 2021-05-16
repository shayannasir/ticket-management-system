package tech.shayannasir.tms.dto;

import lombok.Getter;
import lombok.Setter;
import tech.shayannasir.tms.entity.*;

import java.util.Date;

@Getter
@Setter
public class TicketSummaryDTO {
    Long id;
    String subject;
    TicketStatus status;
    TicketPriority priority;
    TicketSource source;
    Date dueDate;
    Date createdDate;
}
