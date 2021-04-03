package tech.shayannasir.tms.service.Impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.binder.TicketBinder;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;
import tech.shayannasir.tms.service.TicketService;

import java.util.*;

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
    @Autowired
    private TicketBinder dataBinder;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EndUserRepository endUserRepository;

    @Override
    public ResponseDTO createNewTicket(TicketCreateDTO ticketCreateDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));

        validateCreateTicketRequest(ticketCreateDTO, responseDTO);

        if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
            return responseDTO;

        TicketStatus status = ticketStatusRepository.findByValue(ticketCreateDTO.getStatus());
        TicketPriority priority = ticketPriorityRepository.findByValue(ticketCreateDTO.getPriority());
        TicketClassification classification = ticketClassificationRepository.findByValue(ticketCreateDTO.getClassification());

        if (Objects.isNull(status))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_STATUS)));
        if (Objects.isNull(priority))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_PRIORITY)));
        if (Objects.isNull(classification))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_CLASSIFICATION)));

        List<Tag> tags = resourceService.mapTagValueToObjects(ticketCreateDTO.getTags(), responseDTO);

        if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
            return responseDTO;

        EndUser existingEndUser = endUserRepository.findByEmail(ticketCreateDTO.getEmail());
        if (Objects.nonNull(existingEndUser)) {
            if (!isValidEndUserDetails(ticketCreateDTO, existingEndUser)) {
                responseDTO.setMessage("Inconsistent End User Details");
                responseDTO.setData(existingEndUser);
                return responseDTO;
            }
            existingEndUser.setTotalTickets(existingEndUser.getTotalTickets() + 1);
            existingEndUser.setDueTickets(existingEndUser.getDueTickets() + 1);
        } else {
            existingEndUser = EndUser.builder()
                    .name(ticketCreateDTO.getContactName())
                    .number(ticketCreateDTO.getMobile())
                    .email(ticketCreateDTO.getEmail())
                    .workID(ticketCreateDTO.getWorkID())
                    .totalTickets(1L)
                    .dueTickets(1L)
                    .build();
        }

        EndUser savedEndUser = endUserRepository.save(existingEndUser);


        Ticket ticket = Ticket.builder()
                .endUser(savedEndUser)
                .subject(ticketCreateDTO.getSubject())
                .description(ticketCreateDTO.getDescription())
                .dueDate(ticketCreateDTO.getDueDate())
                .assignedOn(new Date())
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

    @Override
    public ResponseDTO fetchTicketDetails(Long id) {

        Optional<Ticket> existingTicket = ticketRepository.findById(id);
        return existingTicket.map(ticket -> new ResponseDTO(
                Boolean.TRUE,
                getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY),
                dataBinder.bindToDTO(ticket))).orElse(new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.TICKET_NOT_FOUND, id)));
    }

    @Override
    public DataTableResponseDTO<Object, List<TicketResponseDTO>> fetchListOfTickets(DataTableRequestDTO dataTableRequestDTO) {
        List<TicketResponseDTO> ticketDTOs = new ArrayList<>();
        List<Ticket> ticketResults;
        long resultCount;
        Sort sort = null;
        if (StringUtils.isNotBlank(dataTableRequestDTO.getSortColumn())) {
            sort = Sort.by(dataTableRequestDTO.getSortDirection(), dataTableRequestDTO.getSortColumn());
        }
        if (BooleanUtils.isTrue(dataTableRequestDTO.getFetchAllRecords())) {
            if (sort != null)
                ticketResults = ticketRepository.findAll(sort);
            else
                ticketResults = ticketRepository.findAll();

            resultCount = ticketResults.size();
        } else {
            Pageable pageable;
            if (sort != null)
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize(), sort);
            else
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize());

            Page<Ticket> ticketPage = ticketRepository.findAll(pageable);
            ticketResults = ticketPage.getContent();
            resultCount = ticketPage.getTotalPages();
        }
        ticketResults.stream().forEach(ticket -> {
            TicketResponseDTO ticketResponseDTO = TicketResponseDTO.builder()
                    .id(ticket.getId())
                    .subject(ticket.getSubject())
                    .status(ticket.getStatus())
                    .priority(ticket.getPriority())
                    .classification(ticket.getClassification())
                    .description(ticket.getDescription())
                    .tags(ticket.getTags())
                    .remarkComments(ticket.getRemarkComments())
                    .resolutionComments(ticket.getResolutionComments())
                    .build();
            ticketDTOs.add(ticketResponseDTO);
        });

        DataTableResponseDTO<Object, List<TicketResponseDTO>> responseDTO = DataTableResponseDTO.getInstance(ticketDTOs, resultCount);
        responseDTO.setRecordsTotal(ticketRepository.count());
        return responseDTO;
    }

    @Override
    public ResponseDTO editTicketDetails(TicketRequestDTO ticketRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.TICKET_NOT_FOUND, ticketRequestDTO.getId()));
        Optional<Ticket> existingTicket = ticketRepository.findById(ticketRequestDTO.getId());

        if (existingTicket.isPresent()) {
            TicketStatus status = ticketStatusRepository.findByValue(ticketRequestDTO.getStatus());
            TicketPriority priority = ticketPriorityRepository.findByValue(ticketRequestDTO.getPriority());
            TicketClassification classification = ticketClassificationRepository.findByValue(ticketRequestDTO.getClassification());

            if (Objects.isNull(status))
                responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_STATUS)));
            if (Objects.isNull(priority))
                responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_PRIORITY)));
            if (Objects.isNull(classification))
                responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_CLASSIFICATION)));

            List<Tag> tags = resourceService.mapTagValueToObjects(ticketRequestDTO.getTags(), responseDTO);

            if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
                responseDTO.setStatus(false);
                responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
                return responseDTO;
            }

            Ticket ticket = existingTicket.get();
//            ticket.setContactName(ticketRequestDTO.getContactName());
//            ticket.setEmail(ticketRequestDTO.getEmail());
//            ticket.setMobile(ticketRequestDTO.getMobile());
//            ticket.setWorkID(ticketRequestDTO.getWorkID());
            ticket.setDescription(ticketRequestDTO.getDescription());
            ticket.setSubject(ticketRequestDTO.getSubject());
            ticket.setStatus(status);
            ticket.setPriority(priority);
            ticket.setClassification(classification);
            ticket.setTags(tags);

            ticketRepository.save(ticket);

            responseDTO.setStatus(Boolean.TRUE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_UPDATE_SUCCESS));
            responseDTO.setData(ticket);
            return responseDTO;

        }
        return responseDTO;
    }

    private void validateCreateTicketRequest(TicketCreateDTO ticketCreateDTO, ResponseDTO responseDTO) {
        if (!ticketCreateDTO.getMobile().matches(Constants.MOBILE_NUMBER_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_MOBILE_NUMBER)));

        if (!ticketCreateDTO.getEmail().matches(Constants.EMAIL_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_EMAIL)));
    }

    private Boolean isValidEndUserDetails(TicketCreateDTO source, EndUser target) {
        return source.getContactName().equals(target.getName())
                && source.getEmail().equals(target.getEmail())
                && source.getMobile().equals(target.getNumber())
                && source.getWorkID().equals(target.getWorkID());
    }
}
