package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.TicketPriority;
import tech.shayannasir.tms.entity.TicketStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequestDTO {

    String name;
    Date dueDate;
    Long ticketNo;
    String description;

    Long assignedTo;
    String priority;
    String status;
    List<String> tags;

}
