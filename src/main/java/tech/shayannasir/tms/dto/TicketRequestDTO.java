package tech.shayannasir.tms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.Tag;
import tech.shayannasir.tms.entity.TicketClassification;
import tech.shayannasir.tms.entity.TicketPriority;
import tech.shayannasir.tms.entity.TicketStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {

    Long id;

    /* End USer Details */
    @NotBlank
    String contactName;
    @NotBlank
    String mobile;
    @NotBlank
    String email;
    @NotBlank
    String workID;

    @NotBlank
    String subject;
    @NotNull
    Date dueDate;
    @NotBlank
    String status;
    @NotBlank
    String priority;
    @NotBlank
    String classification;
    @NotBlank
    String description;
    @NotBlank
    String source;
    @NotNull
    Long assignedTo;
    @NotEmpty
    List<String> tags;
}
