package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketCreateDTO {

    /* End User Details */
    @NotBlank
    String contactName;
    @NotBlank
    String email;
    @NotBlank
    String mobile;
    @NotBlank
    String workID;

    /* Ticket Details */
    @NotBlank
    String subject;
    @NotBlank
    String description;
    @NotNull
    Date dueDate;
    @NotBlank
    String status;
    @NotBlank
    String priority;
    @NotBlank
    String classification;
    @NotBlank
    String source;
    @NotNull
    Long assignedTo;
    @NotEmpty
    List<String> tags;

}
