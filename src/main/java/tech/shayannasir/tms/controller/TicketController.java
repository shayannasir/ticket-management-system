package tech.shayannasir.tms.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TicketCreateDTO;
import tech.shayannasir.tms.dto.TicketDTO;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.TicketService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/ticket")
@Api
public class TicketController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseDTO createTicket (@Valid @RequestBody TicketCreateDTO ticketCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        } else
            return ticketService.createNewTicket(ticketCreateDTO);
    }

}
