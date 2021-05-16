package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.*;
import tech.shayannasir.tms.entity.Task;

import java.util.List;

public interface TaskService {
    ResponseDTO createNewTask(TaskRequestDTO taskRequestDTO);
    ResponseDTO editTaskDetails(TaskRequestDTO taskRequestDTO);
    ResponseDTO fetchTaskDetails(Long id);
    DataTableResponseDTO<Object, List<TaskSummaryDTO>> fetchListOfTask(DataTableRequestDTO dataTableRequestDTO);
    Task validateTask(Long taskID, ResponseDTO responseDTO);
}
