package tech.shayannasir.tms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.entity.Ticket;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndUserDTO {

    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String email;
    @NotBlank
    String number;
    @NotBlank
    String workID;

    Long totalTickets;
    Long dueTickets;

}
