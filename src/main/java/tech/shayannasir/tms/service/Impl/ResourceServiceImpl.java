package tech.shayannasir.tms.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TagDTO;
import tech.shayannasir.tms.entity.Tag;
import tech.shayannasir.tms.repository.TagRepository;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

import java.util.Objects;

@Service
public class ResourceServiceImpl extends MessageService implements ResourceService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public ResponseDTO createNewTag(TagDTO tagDTO) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.TAG_CREATED_SUCCESS));

        Tag tagName = tagRepository.findByName(tagDTO.getName());
        Tag tagValue = tagRepository.findByValue(tagDTO.getValue());

        if (Objects.nonNull(tagName) || Objects.nonNull(tagValue)) {
            responseDTO.setStatus(Boolean.FALSE);
            responseDTO.setMessage(getMessage(MessageConstants.TAG_EXISTS));
            return responseDTO;
        }

        Tag tag = new Tag();
        String value = tagDTO.getName().toLowerCase().replaceAll(" ", "-");

        tag.setName(tagDTO.getName());
        tag.setValue(value);
        tag.setEnabled(Boolean.FALSE);

        tagRepository.save(tag);

        return responseDTO;
    }

}
