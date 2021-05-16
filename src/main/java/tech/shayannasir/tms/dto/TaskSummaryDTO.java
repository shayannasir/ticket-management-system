package tech.shayannasir.tms.dto;

import lombok.Getter;
import lombok.Setter;
import tech.shayannasir.tms.entity.*;

import java.util.Date;

@Getter
@Setter
public class TaskSummaryDTO {

    Long id;
    String name;
    String assignedTo;
    Date assignedOn;
    String description;
    TicketPriority priority;
    TicketStatus status;
    Date dueDate;

}
