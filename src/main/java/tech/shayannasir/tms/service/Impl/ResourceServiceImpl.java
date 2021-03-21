package tech.shayannasir.tms.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.enums.Resource;
import tech.shayannasir.tms.repository.TagRepository;
import tech.shayannasir.tms.repository.TicketClassificationRepository;
import tech.shayannasir.tms.repository.TicketPriorityRepository;
import tech.shayannasir.tms.repository.TicketStatusRepository;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

import java.util.Objects;

@Service
public class ResourceServiceImpl extends MessageService implements ResourceService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TicketClassificationRepository ticketClassificationRepository;
    @Autowired
    private TicketPriorityRepository ticketPriorityRepository;
    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Override
    public ResponseDTO createNewTag(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TAG_CREATED_SUCCESS));

        Tag tagName = tagRepository.findByName(ticketResourceDTO.getName());
        Tag tagValue = tagRepository.findByValue(ticketResourceDTO.getValue());

        if (Objects.nonNull(tagName) || Objects.nonNull(tagValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TAG_EXISTS));
            return responseDTO;
        }

        Tag tag = new Tag();
        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        tag.setName(ticketResourceDTO.getName());
        tag.setValue(value);
        tag.setEnabled(Boolean.FALSE);

        tagRepository.save(tag);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewClassification(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TICKET_CLASSIFICATION_CREATED_SUCCESS));

        TicketClassification classificationName = ticketClassificationRepository.findByName(ticketResourceDTO.getName());
        TicketClassification classificationValue = ticketClassificationRepository.findByValue(ticketResourceDTO.getValue());

        if (Objects.nonNull(classificationName) || Objects.nonNull(classificationValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_CLASSIFICATION_EXISTS));
            return responseDTO;
        }

        TicketClassification ticketClassification = new TicketClassification();
        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        ticketClassification.setName(ticketResourceDTO.getName());
        ticketClassification.setValue(value);
        ticketClassification.setEnabled(Boolean.FALSE);

        ticketClassificationRepository.save(ticketClassification);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewPriority(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TICKET_PRIORITY_CREATED_SUCCESS));

        TicketPriority priorityName = ticketPriorityRepository.findByName(ticketResourceDTO.getName());
        TicketPriority priorityValue = ticketPriorityRepository.findByValue(ticketResourceDTO.getValue());

        if (Objects.nonNull(priorityName) || Objects.nonNull(priorityValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_PRIORITY_EXISTS));
            return responseDTO;
        }

        TicketPriority ticketPriority = new TicketPriority();
        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        ticketPriority.setName(ticketResourceDTO.getName());
        ticketPriority.setValue(value);
        ticketPriority.setEnabled(Boolean.FALSE);

        ticketPriorityRepository.save(ticketPriority);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewStatus(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TICKET_STATUS_CREATED_SUCCESS));

        TicketStatus statusName = ticketStatusRepository.findByName(ticketResourceDTO.getName());
        TicketStatus statusValue = ticketStatusRepository.findByValue(ticketResourceDTO.getValue());

        if (Objects.nonNull(statusName) || Objects.nonNull(statusValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_STATUS_EXISTS));
            return responseDTO;
        }

        TicketStatus ticketStatus = new TicketStatus();
        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        ticketStatus.setName(ticketResourceDTO.getName());
        ticketStatus.setValue(value);
        ticketStatus.setEnabled(Boolean.FALSE);

        ticketStatusRepository.save(ticketStatus);

        return responseDTO;
    }

    @Override
    public ResponseDTO updateEnableStatus(ResourceEnableDTO resourceEnableDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        Resource resource = Resource.valueOf(resourceEnableDTO.getResource().name());
        switch (resource) {
            case TAG :
                Tag existingTag = tagRepository.findByValue(resourceEnableDTO.getValue());
                if (Objects.nonNull(existingTag)) {
                    existingTag.setEnabled(resourceEnableDTO.getEnabled());
                    tagRepository.save(existingTag);
                    return new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.RESOURCE_UPDATE_SUCCESS, resource.name()));
                } else {
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.INVALID_REQUEST, getMessage(MessageConstants.RESOURCE_UPDATE_EXISTS, resource.name())));
                }
                break;
            case STATUS:
                TicketStatus existingStatus = ticketStatusRepository.findByValue(resourceEnableDTO.getValue());
                if (Objects.nonNull(existingStatus)) {
                    existingStatus.setEnabled(resourceEnableDTO.getEnabled());
                    ticketStatusRepository.save(existingStatus);
                    return new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.RESOURCE_UPDATE_SUCCESS, resource.name()));
                } else {
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.INVALID_REQUEST, getMessage(MessageConstants.RESOURCE_UPDATE_EXISTS, resource.name())));
                }
                break;
            case PRIORITY:
                TicketPriority existingPriority = ticketPriorityRepository.findByValue(resourceEnableDTO.getValue());
                if (Objects.nonNull(existingPriority)) {
                    existingPriority.setEnabled(resourceEnableDTO.getEnabled());
                    ticketPriorityRepository.save(existingPriority);
                    return new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.RESOURCE_UPDATE_SUCCESS, resource.name()));
                } else {
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.INVALID_REQUEST, getMessage(MessageConstants.RESOURCE_UPDATE_EXISTS, resource.name())));
                }
                break;
            case CLASSIFICATION:
                TicketClassification existingClassification = ticketClassificationRepository.findByValue(resourceEnableDTO.getValue());
                if (Objects.nonNull(existingClassification)) {
                    existingClassification.setEnabled(resourceEnableDTO.getEnabled());
                    ticketClassificationRepository.save(existingClassification);
                    return new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.RESOURCE_UPDATE_SUCCESS, resource.name()));
                } else {
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.INVALID_REQUEST, getMessage(MessageConstants.RESOURCE_UPDATE_EXISTS, resource.name())));
                }
                break;
            default:
                responseDTO.addToErrors(new ErrorDTO(ErrorCode.INVALID_REQUEST, "Invalid Resource"));
        }
        return responseDTO;
    }

}
