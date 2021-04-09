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
    Date assignedOn;
    Long ticketNo;
    String description;

    Long assignedToID;
    @OneToOne
    TicketPriority priority;
    @OneToOne
    TicketStatus status;
    @ManyToMany
    List<Tag> tags;
    @OneToMany(mappedBy = "task")
    List<Comment> comments;
    @OneToMany(mappedBy = "task")
    List<Activity> activities;

}
