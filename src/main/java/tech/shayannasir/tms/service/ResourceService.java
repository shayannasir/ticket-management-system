package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ResourceEnableDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TicketResourceDTO;

public interface ResourceService {
    ResponseDTO createNewTag(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewClassification(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewPriority(TicketResourceDTO ticketResourceDTO);
    ResponseDTO createNewStatus(TicketResourceDTO ticketResourceDTO);
    ResponseDTO updateEnableStatus(ResourceEnableDTO resourceEnableDTO);
}
