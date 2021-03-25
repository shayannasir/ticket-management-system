package tech.shayannasir.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketCreateDTO {

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
