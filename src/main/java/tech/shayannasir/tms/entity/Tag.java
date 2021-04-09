package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag extends AuditEntity {

    String name;
    String value;
    Boolean enabled;

//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
//                fetch = FetchType.LAZY)
//    @JoinColumn(name = "ticket_id")
//    Ticket ticket;
}
