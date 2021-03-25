package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.*;

import java.util.List;

public interface TicketService {

    ResponseDTO createNewTicket(TicketCreateDTO ticketCreateDTO);
    ResponseDTO fetchTicketDetails(Long id);
    DataTableResponseDTO<Object, List<TicketResponseDTO>> fetchListOfTickets(DataTableRequestDTO dataTableRequestDTO);
    ResponseDTO editTicketDetails(TicketRequestDTO ticketRequestDTO);
}
