package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ActivityRequestDTO;
import tech.shayannasir.tms.dto.ResponseDTO;

public interface ActivityService {
    ResponseDTO createActivity(ActivityRequestDTO activityRequestDTO);
}
