package tech.shayannasir.tms.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ErrorDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TicketCreateDTO;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.TicketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TicketServiceImpl extends MessageService implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketStatusRepository ticketStatusRepository;
    @Autowired
    private TicketPriorityRepository ticketPriorityRepository;
    @Autowired
    private TicketClassificationRepository ticketClassificationRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public ResponseDTO createNewTicket(TicketCreateDTO ticketCreateDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        validateCreateTicketRequest(ticketCreateDTO, responseDTO);
        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
            return responseDTO;
        }
        TicketStatus status = ticketStatusRepository.findByValue(ticketCreateDTO.getStatus());
        TicketPriority priority = ticketPriorityRepository.findByValue(ticketCreateDTO.getPriority());
        TicketClassification classification = ticketClassificationRepository.findByValue(ticketCreateDTO.getClassification());

        if (Objects.isNull(status))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_STATUS)));
        if (Objects.isNull(priority))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_PRIORITY)));
        if (Objects.isNull(classification))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_CLASSIFICATION)));

        List<Tag> tags = new ArrayList<>();
        for (String tagValue : ticketCreateDTO.getTags()) {
            Tag tag = tagRepository.findByValue(tagValue);
            if (Objects.isNull(tag)) {
                responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_TAG)));
                break;
            }
            tags.add(tag);
        }

        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
            return responseDTO;
        }

        Ticket ticket = Ticket.builder()
                .contactName(ticketCreateDTO.getContactName())
                .mobile(ticketCreateDTO.getMobile())
                .email(ticketCreateDTO.getEmail())
                .workID(ticketCreateDTO.getWorkID())
                .subject(ticketCreateDTO.getSubject())
                .description(ticketCreateDTO.getDescription())
                .status(status)
                .priority(priority)
                .classification(classification)
                .tags(tags)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        responseDTO.setData(savedTicket);
        responseDTO.setStatus(Boolean.TRUE);
        responseDTO.setMessage(getMessage(MessageConstants.TICKET_CREATE_SUCCESS));
        return responseDTO;
    }

    private void validateCreateTicketRequest(TicketCreateDTO ticketCreateDTO, ResponseDTO responseDTO) {
        if (!ticketCreateDTO.getMobile().matches(Constants.MOBILE_NUMBER_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_MOBILE_NUMBER)));

        if (!ticketCreateDTO.getEmail().matches(Constants.EMAIL_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_EMAIL)));
    }
}
