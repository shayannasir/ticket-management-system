package tech.shayannasir.tms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import tech.shayannasir.tms.entity.AuditTrail;

@Getter
@Setter
@NoArgsConstructor
public class AuditResponseDTO {

    private Page<AuditTrail> auditTrailPage;
    //
    private Long targetDocumentId;
    private String action;

    public static AuditResponseDTO createWithTarget(Long targetDocumentId) {
        AuditResponseDTO dto = new AuditResponseDTO();
        dto.setTargetDocumentId(targetDocumentId);
        return dto;
    }

    public static AuditResponseDTO createWithTargetAndAction(Long targetDocumentId, String action) {
        AuditResponseDTO dto = new AuditResponseDTO();
        dto.setTargetDocumentId(targetDocumentId);
        dto.setAction(action);
        return dto;
    }
}
