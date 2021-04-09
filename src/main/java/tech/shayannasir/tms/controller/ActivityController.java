package tech.shayannasir.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.shayannasir.tms.dto.ActivityRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.ActivityService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/add")
    public ResponseDTO addNewActivity(@Valid @RequestBody ActivityRequestDTO activityRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        return activityService.createActivity(activityRequestDTO);
    }

}
