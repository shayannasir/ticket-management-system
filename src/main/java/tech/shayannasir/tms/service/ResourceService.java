package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ResourceEnableDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TicketResourceDTO;
import tech.shayannasir.tms.entity.*;

import java.util.List;

public interface ResourceService {
    ResponseDTO createNewTag(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewClassification(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewPriority(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewStatus(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewDepartment(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewTicketSource(TicketResourceDTO ticketResourceDTO);
    ResponseDTO updateEnableStatus(ResourceEnableDTO resourceEnableDTO);

    ResponseDTO getResourceByType(String resourceType);

    List<Tag> mapTagValueToObjects (List<String> tagValues, ResponseDTO responseDTO);
    TicketClassification validateClassification(String classification, ResponseDTO responseDTO);
    TicketPriority validatePriority(String priority, ResponseDTO responseDTO);
    TicketStatus validateStatus(String status, ResponseDTO responseDTO);
    TicketSource validateSource(String source, ResponseDTO responseDTO);

}
