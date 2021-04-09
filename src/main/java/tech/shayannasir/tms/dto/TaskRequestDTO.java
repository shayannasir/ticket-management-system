package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.TicketPriority;
import tech.shayannasir.tms.entity.TicketStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequestDTO {

    Long id;

    @NotBlank
    String name;
    @NotNull
    Date dueDate;
    Long ticketNo;
    @NotBlank
    String description;

    @NotNull
    Long assignedTo;
    @NotBlank
    String priority;
    @NotBlank
    String status;
    @NotNull
    List<String> tags;

}