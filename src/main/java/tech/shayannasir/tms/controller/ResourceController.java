package tech.shayannasir.tms.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TagDTO;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.ResourceService;

import javax.validation.Valid;

@RestController
@RequestMapping("/resource")
@Api
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/tag/create")
    public ResponseDTO createTag(@Valid @RequestBody TagDTO tagDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.INVALID_REQUEST_BODY));
        return resourceService.createNewTag(tagDTO);
    }
}
