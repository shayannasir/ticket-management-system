package tech.shayannasir.tms.binder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.TaskResponseDTO;
import tech.shayannasir.tms.entity.Task;
import tech.shayannasir.tms.entity.User;
import tech.shayannasir.tms.repository.UserRepository;

import java.util.Optional;

@Component
public class TaskBinder {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDataBinder userDataBinder;

    public TaskResponseDTO bindToDTO(Task source) {
        TaskResponseDTO target = new TaskResponseDTO();

        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setAssignedOn(source.getAssignedOn());
        target.setStatus(source.getStatus());
        target.setPriority(source.getPriority());
        target.setTags(source.getTags());
        target.setComments(source.getComments());
        target.setActivities(source.getActivities());
        target.setAttachments(source.getAttachments());
        Optional<User> optionalUser = userRepository.findById(source.getAssignedToID());
        optionalUser.ifPresent(user -> target.setAssignedTo(userDataBinder.bindDocumentToDetailDTO(Optional.of(user).orElse(null))));
        target.setDueDate(source.getDueDate());
        target.setTicketNo(source.getTicketNo());

        return target;
    }

}
