package tech.shayannasir.tms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
public class Attachment extends AuditEntity {

    String name;
    String originalName;
    Long sizeInBytes;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    Task task;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @JsonIgnore
    Ticket ticket;

    public Attachment(String name, String originalName, Long sizeInBytes) {
        this.name = name;
        this.originalName = originalName;
        this.sizeInBytes = sizeInBytes;
    }

    public Attachment(String name, String originalName, Long sizeInBytes, Task task) {
        this.name = name;
        this.originalName = originalName;
        this.sizeInBytes = sizeInBytes;
        this.task = task;
    }

    public Attachment(String name, String originalName, Long sizeInBytes, Ticket ticket) {
        this.name = name;
        this.originalName = originalName;
        this.sizeInBytes = sizeInBytes;
        this.ticket = ticket;
    }
}
