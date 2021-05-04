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
import tech.shayannasir.tms.enums.Role;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;
import tech.shayannasir.tms.service.TicketService;
import tech.shayannasir.tms.service.UserService;

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
    private TicketSourceRepository ticketSourceRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TicketBinder dataBinder;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EndUserRepository endUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public ResponseDTO createNewTicket(TicketRequestDTO ticketCreateDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));

        validateCreateTicketRequest(ticketCreateDTO, responseDTO);

        if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
            return responseDTO;

        TicketStatus status = resourceService.validateStatus(ticketCreateDTO.getStatus(), responseDTO);
        TicketPriority priority = resourceService.validatePriority(ticketCreateDTO.getPriority(), responseDTO);
        TicketClassification classification = resourceService.validateClassification(ticketCreateDTO.getClassification(), responseDTO);
        TicketSource source = resourceService.validateSource(ticketCreateDTO.getSource(), responseDTO);
        List<Tag> tags = resourceService.mapTagValueToObjects(ticketCreateDTO.getTags(), responseDTO);
        User assignedTo = userService.validateUser(ticketCreateDTO.getAssignedTo(), responseDTO);

        if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
            return responseDTO;

        if (Objects.nonNull(assignedTo)) {
            assignedTo.setTotalTickets(assignedTo.getTotalTickets() + 1);
            if (!status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED))
                assignedTo.setDueTickets(assignedTo.getDueTickets() + 1);
            userRepository.save(assignedTo);
        } else {
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_USER_ID));
            return responseDTO;
        }

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
            EndUser newEndUser = EndUser.builder()
                    .name(ticketCreateDTO.getContactName())
                    .number(ticketCreateDTO.getMobile())
                    .email(ticketCreateDTO.getEmail())
                    .workID(ticketCreateDTO.getWorkID())
                    .totalTickets(1L)
                    .dueTickets(1L)
                    .build();

            existingEndUser = endUserRepository.save(newEndUser);
        }

        Ticket ticket = Ticket.builder()
                .endUserID(existingEndUser.getId())
                .subject(ticketCreateDTO.getSubject())
                .description(ticketCreateDTO.getDescription())
                .dueDate(ticketCreateDTO.getDueDate())
                .assignedOn(new Date())
                .assignedToID(ticketCreateDTO.getAssignedTo())
                .status(status)
                .priority(priority)
                .classification(classification)
                .ticketSource(source)
                .tags(tags)
                .build();

        if (status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED)) {
            existingEndUser.setDueTickets(existingEndUser.getDueTickets() - 1);
            endUserRepository.save(existingEndUser);
        }

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
        boolean isAdmin = false;
        User currentUser = userService.getCurrentLoggedInUser();
        if (Objects.nonNull(currentUser) && currentUser.getRole().name().equals(Role.SUPER_ADMIN.name()))
            isAdmin = true;
        if (StringUtils.isNotBlank(dataTableRequestDTO.getSortColumn())) {
            sort = Sort.by(dataTableRequestDTO.getSortDirection(), dataTableRequestDTO.getSortColumn());
        }
        if (BooleanUtils.isTrue(dataTableRequestDTO.getFetchAllRecords())) {
            if (sort != null)
                ticketResults = isAdmin ? ticketRepository.findAll(sort) : ticketRepository.findAllByAssignedToID(currentUser.getId(), sort);
            else
                ticketResults = isAdmin ? ticketRepository.findAll() : ticketRepository.findAllByAssignedToID(currentUser.getId());

            resultCount = ticketResults.size();
        } else {
            Pageable pageable;
            if (sort != null)
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize(), sort);
            else
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize());

            Page<Ticket> ticketPage = isAdmin ? ticketRepository.findAll(pageable) : ticketRepository.findAllByAssignedToID(currentUser.getId(), pageable);
            ticketResults = ticketPage.getContent();
            resultCount = ticketPage.getTotalPages();
        }
        ticketResults.parallelStream().forEach(ticket -> {
            ticketDTOs.add(dataBinder.bindToDTO(ticket));
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
            TicketStatus status = resourceService.validateStatus(ticketRequestDTO.getStatus(), responseDTO);
            TicketPriority priority = resourceService.validatePriority(ticketRequestDTO.getPriority(), responseDTO);
            TicketClassification classification = resourceService.validateClassification(ticketRequestDTO.getClassification(), responseDTO);
            TicketSource source = resourceService.validateSource(ticketRequestDTO.getSource(), responseDTO);
            List<Tag> tags = resourceService.mapTagValueToObjects(ticketRequestDTO.getTags(), responseDTO);
            User assignedTo = userService.validateUser(ticketRequestDTO.getAssignedTo(), responseDTO);

            if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
                return responseDTO;

            EndUser existingEndUser = endUserRepository.findByEmail(ticketRequestDTO.getEmail());
            if (Objects.nonNull(existingEndUser)) {
                if (!isValidEndUserDetails(ticketRequestDTO, existingEndUser)) {
                    responseDTO.setMessage("Inconsistent End User Details");
                    responseDTO.setData(existingEndUser);
                    return responseDTO;
                }
            } else {
                EndUser newEndUser = EndUser.builder()
                        .name(ticketRequestDTO.getContactName())
                        .number(ticketRequestDTO.getMobile())
                        .email(ticketRequestDTO.getEmail())
                        .workID(ticketRequestDTO.getWorkID())
                        .totalTickets(1L)
                        .dueTickets(1L)
                        .build();
                existingEndUser = endUserRepository.save(newEndUser);
            }

            Ticket ticket = existingTicket.get();

            if (!ticket.getAssignedToID().equals(assignedTo.getId())) {
                User oldUser = userService.validateUser(ticket.getAssignedToID(), responseDTO);
                oldUser.setTotalTickets(oldUser.getTotalTickets() - 1);
                oldUser.setDueTickets(oldUser.getDueTickets() - 1);
                assignedTo.setTotalTickets(assignedTo.getTotalTickets() + 1);
                assignedTo.setDueTickets(assignedTo.getDueTickets() + 1);
                userRepository.save(oldUser);
                userRepository.save(assignedTo);
            }

            ticket.setEndUserID(existingEndUser.getId());
            ticket.setDescription(ticketRequestDTO.getDescription());
            ticket.setSubject(ticketRequestDTO.getSubject());
            if (status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED) && !ticket.getStatus().getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED)) {
                existingEndUser.setDueTickets(existingEndUser.getDueTickets() - 1);
                endUserRepository.save(existingEndUser);
                assignedTo.setDueTickets(assignedTo.getDueTickets() - 1);
                userRepository.save(assignedTo);
            }else if (!status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED) && ticket.getStatus().getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED)) {
                existingEndUser.setDueTickets(existingEndUser.getDueTickets() + 1);
                endUserRepository.save(existingEndUser);
                assignedTo.setDueTickets(assignedTo.getDueTickets() + 1);
                userRepository.save(assignedTo);
            }
            ticket.setStatus(status);
            ticket.setPriority(priority);
            ticket.setClassification(classification);
            ticket.setTicketSource(source);
            ticket.setTags(tags);
            if (!ticket.getAssignedToID().equals(ticketRequestDTO.getAssignedTo())) {
                ticket.setAssignedToID(ticketRequestDTO.getAssignedTo());
                ticket.setAssignedOn(new Date());
            }

            ticketRepository.save(ticket);

            responseDTO.setStatus(Boolean.TRUE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_UPDATE_SUCCESS));
            responseDTO.setData(ticket);
            return responseDTO;

        }
        return responseDTO;
    }

    private void validateCreateTicketRequest(TicketRequestDTO ticketCreateDTO, ResponseDTO responseDTO) {
        if (!ticketCreateDTO.getMobile().matches(Constants.MOBILE_NUMBER_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_MOBILE_NUMBER)));

        if (!ticketCreateDTO.getEmail().matches(Constants.EMAIL_PATTERN))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.INVALID_EMAIL)));
    }

    private Boolean isValidEndUserDetails(TicketRequestDTO source, EndUser target) {
        return source.getContactName().equals(target.getName())
                && source.getEmail().equals(target.getEmail())
                && source.getMobile().equals(target.getNumber())
                && source.getWorkID().equals(target.getWorkID());
    }

    public Ticket validateTicket(Long ticketID, ResponseDTO responseDTO) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketID);
        if (optionalTicket.isPresent())
            return optionalTicket.get();
        responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Ticket ID"));
        return null;
    }

}
