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
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {

    @NotNull
    Long id;
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
    String status;
    @NotNull
    String priority;
    @NotNull
    String classification;
    @NotBlank
    String description;
    @NotEmpty
    List<String> tags;
}
