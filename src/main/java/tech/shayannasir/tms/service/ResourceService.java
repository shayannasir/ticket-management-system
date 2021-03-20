package tech.shayannasir.tms.service;

import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.dto.TagDTO;

public interface ResourceService {
    ResponseDTO createNewTag(TagDTO tagDTO);
}
