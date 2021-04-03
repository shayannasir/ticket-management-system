package tech.shayannasir.tms.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.binder.TicketBinder;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;
import tech.shayannasir.tms.service.TaskService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private TicketBinder dataBinder;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public ResponseDTO createNewTask(TaskRequestDTO taskRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        TicketStatus status = ticketStatusRepository.findByValue(taskRequestDTO.getStatus());
        TicketPriority priority = ticketPriorityRepository.findByValue(taskRequestDTO.getPriority());
        Optional<Ticket> ticket = ticketRepository.findById(taskRequestDTO.getTicketNo());
        Optional<User> user = userRepository.findById(taskRequestDTO.getAssignedTo());

        if (Objects.isNull(status))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Task Status"));
        if (Objects.isNull(priority))
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Task Priority"));
        if (!ticket.isPresent())
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid Ticket ID"));
        if (!user.isPresent())
            responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, "Invalid User ID"));


        List<Tag> tags = resourceService.mapTagValueToObjects(taskRequestDTO.getTags(), responseDTO);

        if (!CollectionUtils.isEmpty(responseDTO.getErrors())) {
            responseDTO.setStatus(false);
            responseDTO.setMessage(getMessage(MessageConstants.INVALID_REQUEST));
            return responseDTO;
        }

        Task task = Task.builder()
                .name(taskRequestDTO.getName())
                .dueDate(taskRequestDTO.getDueDate())
                .ticketNo(taskRequestDTO.getTicketNo())
                .description(taskRequestDTO.getDescription())
                .assignedTo(user.get())
                .priority(priority)
                .status(status)
                .tags(tags)
                .build();

        taskRepository.save(task);
        responseDTO.setStatus(Boolean.TRUE);
        responseDTO.setMessage("Task Created Successfully");
        return responseDTO;
    }


    @Override
    public ResponseDTO editTaskDetails(TaskRequestDTO taskRequestDTO) {
        return null;
    }

    @Override
    public ResponseDTO fetchTaskDetails(Long id) {
        return null;
    }

    @Override
    public DataTableResponseDTO<Object, List<TaskRequestDTO>> fetchListOfTask(DataTableRequestDTO dataTableRequestDTO) {
        return null;
    }
}
