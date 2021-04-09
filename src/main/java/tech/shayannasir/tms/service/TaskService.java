package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.*;

import java.util.List;

public interface TaskService {
    ResponseDTO createNewTask(TaskRequestDTO taskRequestDTO);
    ResponseDTO editTaskDetails(TaskRequestDTO taskRequestDTO);
    ResponseDTO fetchTaskDetails(Long id);
    DataTableResponseDTO<Object, List<TaskResponseDTO>> fetchListOfTask(DataTableRequestDTO dataTableRequestDTO);
}
