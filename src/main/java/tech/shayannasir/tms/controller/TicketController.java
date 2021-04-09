package tech.shayannasir.tms.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.TicketService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/ticket")
@Api
public class TicketController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseDTO createTicket (@Valid @RequestBody TicketRequestDTO ticketCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        } else
            return ticketService.createNewTicket(ticketCreateDTO);
    }

    @PostMapping("/update")
    public ResponseDTO updateTicket(@Valid @RequestBody TicketRequestDTO ticketRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        } else {
            if (Objects.nonNull(ticketRequestDTO.getId()))
                return ticketService.editTicketDetails(ticketRequestDTO);
            return new ResponseDTO(Boolean.FALSE, "Ticket ID cannot be NULL");
        }
    }

    @GetMapping("/details/{id}")
    public ResponseDTO getTicketDetails(@PathVariable String id) {
        try {
            Long ID = Long.parseLong(id.trim());
            return ticketService.fetchTicketDetails(ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_TICKET_ID));
        }
    }

    @PostMapping("/list")
    public DataTableResponseDTO<Object, List<TicketResponseDTO>> getTicketList(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
        return ticketService.fetchListOfTickets(dataTableRequestDTO);
    }

}
