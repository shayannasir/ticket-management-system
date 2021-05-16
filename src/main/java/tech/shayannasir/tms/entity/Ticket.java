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

    Long endUserID;

    String subject;

    @Column(columnDefinition = "TEXT")
    String description;

    Date dueDate;
    Date assignedOn;

    Long assignedToID;

    @OneToOne
    TicketStatus status;

    @OneToOne
    TicketPriority priority;

    @OneToOne
    TicketClassification classification;

    @OneToOne
    TicketSource ticketSource;

    @ManyToMany
    List<Tag> tags;

    @OneToMany(mappedBy = "ticket")
    List<Comment> comments;

    @OneToMany(mappedBy = "ticket")
    List<Activity> activities;

    @OneToMany(mappedBy = "ticket")
    List<Attachment> attachments;

}
