package tech.shayannasir.tms.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class TicketSource extends AuditEntity {

    String name;
    String value;
    Boolean enabled;

}
