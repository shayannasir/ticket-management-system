package tech.shayannasir.tms.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ResourceEnableDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TagDTO;
import tech.shayannasir.tms.dto.TicketResourceDTO;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

import javax.validation.Valid;

@RestController
@RequestMapping("/resource")
@Api
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/create/tag")
    public ResponseDTO createTag(@Valid @RequestBody TicketResourceDTO ticketResourceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST_BODY));
        return resourceService.createNewTag(ticketResourceDTO);
    }

    @PostMapping("/create/ticket/classification")
    public ResponseDTO createTicketClassification(@Valid @RequestBody TicketResourceDTO ticketResourceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST_BODY));
        return resourceService.createNewClassification(ticketResourceDTO);
    }

    @PostMapping("/create/ticket/priority")
    public ResponseDTO createTicketPriority(@Valid @RequestBody TicketResourceDTO ticketResourceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST_BODY));
        return resourceService.createNewPriority(ticketResourceDTO);
    }

    @PostMapping("/create/ticket/status")
    public ResponseDTO createTicketStatus(@Valid @RequestBody TicketResourceDTO ticketResourceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST_BODY));
        return resourceService.createNewStatus(ticketResourceDTO);
    }

    /* For Enabling Tag, Classification, Priority and Status */
    @PostMapping("/enable")
    public ResponseDTO enableResource(@Valid @RequestBody ResourceEnableDTO resourceEnableDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST_BODY));
        return resourceService.updateEnableStatus(resourceEnableDTO);
    }

    @GetMapping("/fetch/{resourceType}")
    public ResponseDTO getResource(@PathVariable String resourceType) {
        if (StringUtils.isNotBlank(resourceType))
            return resourceService.getResourceByType(resourceType);
        else
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST));
    }
}
