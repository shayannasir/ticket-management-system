package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag extends AuditEntity {

    String name;

    String value;

    Boolean enabled;

}
