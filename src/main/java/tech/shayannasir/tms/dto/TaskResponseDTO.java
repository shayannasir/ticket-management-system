package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.Comment;
import tech.shayannasir.tms.entity.Tag;
import tech.shayannasir.tms.entity.TicketPriority;
import tech.shayannasir.tms.entity.TicketStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponseDTO {

    Long id;

    String name;

    Date assignedOn;

    Long ticketNo;

    String description;

    TicketPriority priority;

    TicketStatus status;

    UserDetailDTO assignedTo;

    List<Tag> tags;

    List<Comment> comments;

    Date dueDate;



}
