package tech.shayannasir.tms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    String contactName;
    String mobile;
    String email;
    String workID;
    String subject;
    String description;

    @ManyToOne
    TicketStatus status;

    @ManyToOne
    TicketPriority priority;

    @ManyToOne
    TicketClassification classification;

    @ManyToMany
    List<Tag> tags;

    @OneToMany
    @Cascade(CascadeType.ALL)
    List<Comment> remarkComments;

    @OneToMany
    @Cascade(CascadeType.ALL)
    List<Comment> resolutionComments;



}
