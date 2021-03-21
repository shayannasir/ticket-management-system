package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TicketCreateDTO;
import tech.shayannasir.tms.dto.TicketDTO;

public interface TicketService {
    ResponseDTO createNewTicket(TicketCreateDTO ticketCreateDTO);
}
