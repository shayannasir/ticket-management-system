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
import tech.shayannasir.tms.binder.TaskBinder;
import tech.shayannasir.tms.binder.TicketBinder;
import tech.shayannasir.tms.constants.Constants;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.enums.Role;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.*;

import java.util.*;

@Service
public class TaskServiceImpl extends MessageService implements TaskService {

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
    private TaskBinder dataBinder;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @Override
    public ResponseDTO createNewTask(TaskRequestDTO taskRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));
        TicketStatus status = resourceService.validateStatus(taskRequestDTO.getStatus(), responseDTO);
        TicketPriority priority = resourceService.validatePriority(taskRequestDTO.getPriority(), responseDTO);
        List<Tag> tags = resourceService.mapTagValueToObjects(taskRequestDTO.getTags(), responseDTO);
        User assignedTo = userService.validateUser(taskRequestDTO.getAssignedTo(), responseDTO);
        Ticket ticket = null;
        if (Objects.nonNull(taskRequestDTO.getTicketNo()))
            ticket = ticketService.validateTicket(taskRequestDTO.getTicketNo(), responseDTO);

        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
            return responseDTO;
        }

        if (Objects.nonNull(assignedTo)) {
            assignedTo.setTotalTasks(assignedTo.getTotalTasks() + 1);
            if (!status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED))
                assignedTo.setDueTasks(assignedTo.getDueTasks() + 1);
            userRepository.save(assignedTo);
        } else {
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_USER_ID));
            return responseDTO;
        }

        Task task = Task.builder()
                .name(taskRequestDTO.getName())
                .dueDate(taskRequestDTO.getDueDate())
                .ticketNo(taskRequestDTO.getTicketNo())
                .description(taskRequestDTO.getDescription())
                .assignedToID(taskRequestDTO.getAssignedTo())
                .priority(priority)
                .status(status)
                .tags(tags)
                .assignedOn(new Date())
                .build();

        taskRepository.save(task);
        responseDTO.setStatus(Boolean.TRUE);
        responseDTO.setMessage("Task Created Successfully");
        return responseDTO;
    }

    @Override
    public ResponseDTO editTaskDetails(TaskRequestDTO taskRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, "Task Not Found by ID");

        if (Objects.nonNull(taskRequestDTO.getId())) {
            Optional<Task> optionalTask = taskRepository.findById(taskRequestDTO.getId());

            if (optionalTask.isPresent()) {
                TicketStatus status = resourceService.validateStatus(taskRequestDTO.getStatus(), responseDTO);
                TicketPriority priority = resourceService.validatePriority(taskRequestDTO.getPriority(), responseDTO);
                List<Tag> tags = resourceService.mapTagValueToObjects(taskRequestDTO.getTags(), responseDTO);
                User assignedTo = userService.validateUser(taskRequestDTO.getAssignedTo(), responseDTO);
                Ticket ticket = null;
                if (Objects.nonNull(taskRequestDTO.getTicketNo()))
                    ticket = ticketService.validateTicket(taskRequestDTO.getTicketNo(), responseDTO);

                if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
                    responseDTO.setStatus(false);
                    responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
                    return responseDTO;
                }

                Task task = optionalTask.get();

                if (!task.getAssignedToID().equals(assignedTo.getId())) {
                    User oldUser = userService.validateUser(task.getAssignedToID(), responseDTO);
                    oldUser.setTotalTasks(oldUser.getTotalTasks() - 1);
                    assignedTo.setTotalTasks(assignedTo.getTotalTasks() + 1);
                    if (!task.getStatus().getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED)) {
                        oldUser.setDueTasks(oldUser.getDueTasks() - 1);
                        assignedTo.setDueTasks(assignedTo.getDueTasks() + 1);
                    }
                    userRepository.save(oldUser);
                    userRepository.save(assignedTo);
                }

                task.setName(taskRequestDTO.getName());
                task.setDueDate(taskRequestDTO.getDueDate());
                task.setTicketNo(taskRequestDTO.getTicketNo());
                task.setDescription(taskRequestDTO.getDescription());
                task.setAssignedToID(taskRequestDTO.getAssignedTo());
                task.setPriority(priority);

                if (status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED) && !task.getStatus().getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED)) {
                    assignedTo.setDueTasks(assignedTo.getDueTasks() - 1);
                    userRepository.save(assignedTo);
                } else if (!status.getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED) && task.getStatus().getValue().equalsIgnoreCase(Constants.TICKET_STATUS_CLOSED)) {
                    assignedTo.setDueTasks(assignedTo.getDueTasks() + 1);
                    userRepository.save(assignedTo);
                }

                task.setStatus(status);
                task.setTags(tags);
                if (!task.getAssignedToID().equals(taskRequestDTO.getAssignedTo())) {
                    task.setAssignedOn(new Date());
                    task.setAssignedToID(taskRequestDTO.getAssignedTo());
                }

                taskRepository.save(task);
                responseDTO.setStatus(Boolean.TRUE);
                responseDTO.setMessage("Task Updated Successfully");

            }
        } else {
            responseDTO.setMessage("Invalid Task ID");
        }

        return responseDTO;
    }

    @Override
    public ResponseDTO fetchTaskDetails(Long id) {

        Optional<Task> existingTask = taskRepository.findById(id);
        return existingTask.map(task -> new ResponseDTO(
                Boolean.TRUE,
                getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY),
                dataBinder.bindToDTO(task))).orElse(new ResponseDTO(Boolean.FALSE, "Task Not Found with ID"));
    }

    @Override
    public DataTableResponseDTO<Object, List<TaskResponseDTO>> fetchListOfTask(DataTableRequestDTO dataTableRequestDTO) {
        List<TaskResponseDTO> taskDTOs = new ArrayList<>();
        List<Task> taskResults;
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
                taskResults = isAdmin ? taskRepository.findAll(sort) : taskRepository.findAllByAssignedToID(currentUser.getId(), sort);
            else
                taskResults = isAdmin ? taskRepository.findAll() : taskRepository.findAllByAssignedToID(currentUser.getId());

            resultCount = taskResults.size();
        } else {
            Pageable pageable;
            if (sort != null)
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize(), sort);
            else
                pageable = PageRequest.of(dataTableRequestDTO.getPageIndex(), dataTableRequestDTO.getPageSize());

            Page<Task> taskPage = isAdmin ? taskRepository.findAll(pageable) : taskRepository.findAllByAssignedToID(currentUser.getId(), pageable);
            taskResults = taskPage.getContent();
            resultCount = taskPage.getTotalPages();
        }
        taskResults.parallelStream().forEach(task -> {
            taskDTOs.add(dataBinder.bindToDTO(task));
        });

        DataTableResponseDTO<Object, List<TaskResponseDTO>> responseDTO = DataTableResponseDTO.getInstance(taskDTOs, resultCount);
        responseDTO.setRecordsTotal(ticketRepository.count());
        return responseDTO;
    }

    public Task validateTask(Long taskID, ResponseDTO responseDTO) {
        Optional<Task> optionalTask = taskRepository.findById(taskID);
        if (optionalTask.isPresent())
            return optionalTask.get();
        responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Task ID"));
        return null;
    }
}
