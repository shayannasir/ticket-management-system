package tech.shayannasir.tms.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.shayannasir.tms.binder.ActivityBinder;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ActivityRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.entity.Activity;
import tech.shayannasir.tms.entity.Task;
import tech.shayannasir.tms.entity.Ticket;
import tech.shayannasir.tms.enums.ActivitySource;
import tech.shayannasir.tms.repository.ActivityRepository;
import tech.shayannasir.tms.repository.TaskRepository;
import tech.shayannasir.tms.repository.TicketRepository;
import tech.shayannasir.tms.service.ActivityService;
import tech.shayannasir.tms.service.MessageService;

import java.util.Optional;

@Service
public class ActivityServiceImpl extends MessageService implements ActivityService {

    @Autowired
    private ActivityBinder activityBinder;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public ResponseDTO createActivity(ActivityRequestDTO activityRequestDTO) {
        ActivitySource source = ActivitySource.valueOf(activityRequestDTO.getSource().name());
        ResponseDTO responseDTO = new ResponseDTO();
        Activity activity = activityBinder.bindToDocument(activityRequestDTO);

        switch (source) {
            case TICKET:
                Optional<Ticket> existingTicket = ticketRepository.findById(activityRequestDTO.getSourceID());
                if (existingTicket.isPresent()) {
                    activity.setTicket(existingTicket.get());
                    activityRepository.save(activity);
                    responseDTO.setStatus(Boolean.TRUE);
                    responseDTO.setMessage("Activity Added Successfully");
                    return responseDTO;
                } else
                    return new ResponseDTO(Boolean.FALSE, "No Ticket Found with the ID");
            case TASK:
                Optional<Task> existingTask = taskRepository.findById(activityRequestDTO.getSourceID());
                if (existingTask.isPresent()) {
                    activity.setTask(existingTask.get());
                    activityRepository.save(activity);
                    responseDTO.setStatus(Boolean.TRUE);
                    responseDTO.setMessage("Activity Added Successfully");
                    return responseDTO;
                } else
                    return new ResponseDTO(Boolean.FALSE, "No Task Found with the ID");
        }
        return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));
    }
}
