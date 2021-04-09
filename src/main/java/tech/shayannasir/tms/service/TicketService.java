package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.Ticket;

import java.util.List;

public interface TicketService {

    ResponseDTO createNewTicket(TicketRequestDTO ticketCreateDTO);
    ResponseDTO fetchTicketDetails(Long id);
    DataTableResponseDTO<Object, List<TicketResponseDTO>> fetchListOfTickets(DataTableRequestDTO dataTableRequestDTO);
    ResponseDTO editTicketDetails(TicketRequestDTO ticketRequestDTO);

    Ticket validateTicket(Long ticketID, ResponseDTO responseDTO);
}
