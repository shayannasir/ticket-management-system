package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task extends AuditEntity {

    String name;
    Date dueDate;
    Long ticketNo;
    String description;

    @ManyToOne(cascade = CascadeType.ALL)
    User assignedTo;
    @OneToOne
    TicketPriority priority;
    @OneToOne
    TicketStatus status;
    @OneToMany
    List<Tag> tags;
    @OneToMany
    List<Comment> resolutionComments;
    @OneToMany
    List<Comment> remarkComment;

}
