package tech.shayannasir.tms.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "audit_trail")
public class AuditTrail extends AuditEntity<String> {

    @Id
    Long documentId;
    String userName;
    String userRole;
    String module;
    String action;
    String ipAddress;

    public AuditTrail(String userName, String userRole, String ipAddress, String module, String action) {
        super();
        this.module = module;
        this.userName = userName;
        this.userRole = userRole;
        this.ipAddress = ipAddress;
        this.action = action;
    }

    public AuditTrail(String ipAddress, String module, String action) {
        super();
        this.module = module;
        this.ipAddress = ipAddress;
        this.action = action;
    }

}
