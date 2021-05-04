package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EndUser extends AuditEntity{

    String name;
    String email;
    String number;
    String workID;

    Long totalTickets;
    Long dueTickets;

}
