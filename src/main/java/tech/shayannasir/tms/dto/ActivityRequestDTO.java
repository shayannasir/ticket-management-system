package tech.shayannasir.tms.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tech.shayannasir.tms.enums.ActivitySource;
import tech.shayannasir.tms.enums.ActivityStatus;
import tech.shayannasir.tms.enums.ActivityType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityRequestDTO {

    @NotBlank
    String body;
    @NotNull
    ActivitySource source;
    @NotNull
    ActivityType type;
    @NotNull
    Long sourceID;

    ActivityStatus status;

}
