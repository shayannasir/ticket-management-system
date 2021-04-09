package tech.shayannasir.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.enums.ErrorCode;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.TaskService;
import tech.shayannasir.tms.util.ErrorUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseDTO createTask (@Valid @RequestBody TaskRequestDTO taskRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        } else
            return taskService.createNewTask(taskRequestDTO);
    }

    @PostMapping("/update")
    public ResponseDTO updateTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ErrorUtil.bindErrorResponse(ErrorCode.VALIDATION_ERROR, bindingResult);
        } else
            return taskService.editTaskDetails(taskRequestDTO);
    }

    @GetMapping("/details/{id}")
    public ResponseDTO getTaskDetails(@PathVariable String id) {
        try {
            Long ID = Long.parseLong(id.trim());
            return taskService.fetchTaskDetails(ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, "Invalid Task ID");
        }
    }

    @PostMapping("/list")
    public DataTableResponseDTO<Object, List<TaskResponseDTO>> getTaskList(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
        return taskService.fetchListOfTask(dataTableRequestDTO);
    }

}
