package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.DataTableRequestDTO;
import tech.shayannasir.tms.dto.DataTableResponseDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TaskRequestDTO;

import java.util.List;

public interface TaskService {
    ResponseDTO createNewTask(TaskRequestDTO taskRequestDTO);
    ResponseDTO editTaskDetails(TaskRequestDTO taskRequestDTO);
    ResponseDTO fetchTaskDetails(Long id);
    DataTableResponseDTO<Object, List<TaskRequestDTO>> fetchListOfTask(DataTableRequestDTO dataTableRequestDTO);
}
