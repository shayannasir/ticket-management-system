package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResourceDTO {

    /*
    * Common DTO for Tag, TicketClassification, TicketPriority and TicketStatus
    *
    * */

    @NotBlank
    String name;
    String value;
    Boolean enabled;
}
