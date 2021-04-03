package tech.shayannasir.tms.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.enums.Resource;
import tech.shayannasir.tms.repository.*;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

import java.util.*;

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
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TicketSourceRepository ticketSourceRepository;

    @Override
    public ResponseDTO createNewTag(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TAG_CREATED_SUCCESS));

        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        Tag tagValue = tagRepository.findByValue(value);

        if (Objects.nonNull(tagValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TAG_EXISTS));
            return responseDTO;
        }

        Tag tag = new Tag();

        tag.setName(ticketResourceDTO.getName());
        tag.setValue(value);
        tag.setEnabled(Boolean.TRUE);

        tagRepository.save(tag);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewClassification(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TICKET_CLASSIFICATION_CREATED_SUCCESS));

        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        TicketClassification classificationValue = ticketClassificationRepository.findByValue(value);

        if (Objects.nonNull(classificationValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_CLASSIFICATION_EXISTS));
            return responseDTO;
        }

        TicketClassification ticketClassification = new TicketClassification();

        ticketClassification.setName(ticketResourceDTO.getName());
        ticketClassification.setValue(value);
        ticketClassification.setEnabled(Boolean.TRUE);

        ticketClassificationRepository.save(ticketClassification);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewPriority(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TICKET_PRIORITY_CREATED_SUCCESS));

        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        TicketPriority priorityValue = ticketPriorityRepository.findByValue(value);

        if (Objects.nonNull(priorityValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_PRIORITY_EXISTS));
            return responseDTO;
        }

        TicketPriority ticketPriority = new TicketPriority();

        ticketPriority.setName(ticketResourceDTO.getName());
        ticketPriority.setValue(value);
        ticketPriority.setEnabled(Boolean.TRUE);

        ticketPriorityRepository.save(ticketPriority);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewStatus(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TICKET_STATUS_CREATED_SUCCESS));

        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");

        TicketStatus statusValue = ticketStatusRepository.findByValue(value);

        if (Objects.nonNull(statusValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TICKET_STATUS_EXISTS));
            return responseDTO;
        }

        TicketStatus ticketStatus = new TicketStatus();

        ticketStatus.setName(ticketResourceDTO.getName());
        ticketStatus.setValue(value);
        ticketStatus.setEnabled(Boolean.TRUE);

        ticketStatusRepository.save(ticketStatus);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewDepartment(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, "Department Created Successfully");

        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");
        Department existing = departmentRepository.findByValue(value);

        if (Objects.nonNull(existing)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage("Department Already Exists");
            return responseDTO;
        }

        Department department = new Department();

        department.setName(ticketResourceDTO.getName());
        department.setValue(value);
        department.setEnabled(Boolean.TRUE);

        departmentRepository.save(department);

        return responseDTO;
    }

    @Override
    public ResponseDTO createNewTicketSource(TicketResourceDTO ticketResourceDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, "Ticket Source Created Successfully");

        String value = ticketResourceDTO.getName().toLowerCase().replaceAll(" ", "-");
        TicketSource existing = ticketSourceRepository.findByValue(value);

        if (Objects.nonNull(existing)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage("Ticket Source Already Exists");
            return responseDTO;
        }

        TicketSource ticketSource = new TicketSource();

        ticketSource.setName(ticketResourceDTO.getName());
        ticketSource.setValue(value);
        ticketSource.setEnabled(Boolean.TRUE);

        ticketSourceRepository.save(ticketSource);

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
            case DEPARTMENT:
                Department department = departmentRepository.findByValue(resourceEnableDTO.getValue());
                if (Objects.nonNull(department)) {
                    department.setEnabled(resourceEnableDTO.getEnabled());
                    departmentRepository.save(department);
                    return new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.RESOURCE_UPDATE_SUCCESS, resource.name()));
                } else {
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.INVALID_REQUEST, getMessage(MessageConstants.RESOURCE_UPDATE_EXISTS, resource.name())));
                }
                break;
            case SOURCE:
                TicketSource ticketSource = ticketSourceRepository.findByValue(resourceEnableDTO.getValue());
                if (Objects.nonNull(ticketSource)) {
                    ticketSource.setEnabled(resourceEnableDTO.getEnabled());
                    ticketSourceRepository.save(ticketSource);
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

    @Override
    public ResponseDTO getResourceByType(String resourceType) {
        try {
            Resource resource = Resource.valueOf(resourceType);
            ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
            switch (resource) {
                case TAG:
                    List<Tag> tags = tagRepository.findAll();
                    if (!CollectionUtils.isEmpty(tags))
                        responseDTO.setData(tags);
                    else
                        return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.NO_TAG));
                    break;
                case STATUS:
                    List<TicketStatus> statuses = ticketStatusRepository.findAll();
                    if (!CollectionUtils.isEmpty(statuses))
                        responseDTO.setData(statuses);
                    else
                        return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.NO_STATUS));
                    break;
                case PRIORITY:
                    List<TicketPriority> priorities = ticketPriorityRepository.findAll();
                    if (!CollectionUtils.isEmpty(priorities))
                        responseDTO.setData(priorities);
                    else
                        return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.NO_PRIORITY));
                    break;
                case CLASSIFICATION:
                    List<TicketClassification> classifications = ticketClassificationRepository.findAll();
                    if (!CollectionUtils.isEmpty(classifications))
                        responseDTO.setData(classifications);
                    else
                        return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.NO_CLASSIFICATION));
                    break;
                case DEPARTMENT:
                    List<Department> departments = departmentRepository.findAll();
                    if (!CollectionUtils.isEmpty(departments))
                        responseDTO.setData(departments);
                    else
                        return new ResponseDTO(Boolean.FALSE, "No Departments Found");
                    break;
                case SOURCE:
                    List<TicketSource> ticketSources = ticketSourceRepository.findAll();
                    if (!CollectionUtils.isEmpty(ticketSources))
                        responseDTO.setData(ticketSources);
                    else
                        return new ResponseDTO(Boolean.FALSE, "No Ticket Sources Found");
                    break;
            }
            return responseDTO;
        } catch (IllegalArgumentException e) {
            return new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));
        }
    }

    public List<Tag> mapTagValueToObjects(List<String> tagValues, ResponseDTO responseDTO) {
        if (CollectionUtils.isEmpty(tagValues)) {
            return new ArrayList<>(Collections.emptyList());
        } else {
            List<Tag> tags = new ArrayList<>();
            for (String tagValue : tagValues) {
                Tag tag = tagRepository.findByValue(tagValue);
                if (Objects.isNull(tag)) {
                    responseDTO.addToErrors(new ErrorDTO(ErrorCode.VALIDATION_ERROR, getMessage(MessageConstants.TICKET_INVALID_TAG)));
                    break;
                }
                tags.add(tag);
            }
            return tags;
        }
    }

}
