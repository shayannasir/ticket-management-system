package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ResourceEnableDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TicketResourceDTO;
import tech.shayannasir.tms.entity.Tag;

import java.util.List;

public interface ResourceService {
    ResponseDTO createNewTag(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewClassification(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewPriority(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewStatus(TicketResourceDTO ticketResourceDTO);
    ResponseDTO updateEnableStatus(ResourceEnableDTO resourceEnableDTO);
    ResponseDTO getResourceByType(String resourceType);

    List<Tag> mapTagValueToObjects (List<String> tagValues, ResponseDTO responseDTO);
}
