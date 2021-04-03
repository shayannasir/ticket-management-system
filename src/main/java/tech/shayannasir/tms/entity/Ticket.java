package tech.shayannasir.tms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends AuditEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enduser_id")
    EndUser endUser;

    String subject;
    String description;

    Date dueDate;
    Date assignedOn;

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    User assignedTo;

    @OneToOne
    TicketStatus status;

    @OneToOne
    TicketPriority priority;

    @OneToOne
    TicketClassification classification;

    @OneToOne
    TicketSource ticketSource;

    @OneToMany
    List<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL)
    List<Comment> remarkComments;

    @OneToMany(cascade = CascadeType.ALL)
    List<Comment> resolutionComments;

}
