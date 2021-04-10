package tech.shayannasir.tms.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tech.shayannasir.tms.binder.AttachmentBinder;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.entity.*;
import tech.shayannasir.tms.repository.AttachmentRepository;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.service.TaskService;
import tech.shayannasir.tms.service.TicketService;
import tech.shayannasir.tms.service.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileManager extends MessageService {

    @Value("${file.upload.path.local}")
    private String basePath;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private AttachmentBinder attachmentBinder;
    @Autowired
    private TaskService taskService;

    public ResponseDTO saveUserCover(MultipartFile multipartFile) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
        String originalName = multipartFile.getOriginalFilename();
        String filename = generateRandomUUID() + "." + getExtension(originalName);
        Path path = Paths.get(basePath + "user/" + filename);
        try {
            Files.createDirectories(path);
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseDTO(Boolean.FALSE, "Something went wrong. Please try again");
        }
        responseDTO.setData(new FileInfo(filename, originalName, multipartFile.getSize()));
        return responseDTO;
    }

    public InputStream getUserCover(String filename) throws IOException {
        try {
            File file = new File(basePath + "user/" + filename);
            return FileUtils.openInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found", e);
        }
    }

    public ResponseDTO saveArticleCover(MultipartFile multipartFile) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.TRUE, getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
        String originalName = multipartFile.getOriginalFilename();
        String filename = generateRandomUUID() + "." + getExtension(originalName);
        Path path = Paths.get(basePath + "article/" + filename);
        try {
            Files.createDirectories(path);
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseDTO(Boolean.FALSE, "Something went wrong. Please try again");
        }
        responseDTO.setData(new FileInfo(filename, originalName, multipartFile.getSize()));
        return responseDTO;
    }

    public InputStream getArticleCover(String filename) throws IOException {
        try {
            File file = new File(basePath + "article/" + filename);
            return FileUtils.openInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found", e);
        }
    }

    public ResponseDTO saveTicketAttachment(MultipartFile file, Long id) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));

        if (Objects.nonNull(id)) {
            Ticket ticket = ticketService.validateTicket(id, responseDTO);
            if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
                return responseDTO;

            String originalName = file.getOriginalFilename();
            String filename = generateRandomUUID() + "." + getExtension(originalName);
            Path path = Paths.get(basePath + "ticket/" + filename);
            try {
                Files.createDirectories(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseDTO(Boolean.FALSE, "Something went wrong. Please try again");
            }

            attachmentRepository.save(new Attachment(filename, originalName, file.getSize(), ticket));
            responseDTO.setStatus(Boolean.TRUE);
            responseDTO.setMessage(getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
            responseDTO.setData(new FileInfo(filename, originalName, file.getSize()));
        }
        return responseDTO;
    }

    public InputStream getTicketAttachment(String filename) throws IOException {
        try {
            File file = new File(basePath + "ticket/" + filename);
            return FileUtils.openInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found", e);
        }
    }

    public ResponseDTO saveTaskAttachment(MultipartFile file, Long id) {
        ResponseDTO responseDTO = new ResponseDTO(Boolean.FALSE, getMessage(MessageConstants.INVALID_REQUEST));

        if (Objects.nonNull(id)) {
            Task task = taskService.validateTask(id, responseDTO);
            if (!CollectionUtils.isEmpty(responseDTO.getErrors()))
                return responseDTO;

            String originalName = file.getOriginalFilename();
            String filename = generateRandomUUID() + "." + getExtension(originalName);
            Path path = Paths.get(basePath + "task/" + filename);
            try {
                Files.createDirectories(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseDTO(Boolean.FALSE, "Something went wrong. Please try again");
            }
            attachmentRepository.save(new Attachment(filename, originalName, file.getSize(), task));
            responseDTO.setStatus(Boolean.TRUE);
            responseDTO.setMessage(getMessage(MessageConstants.REQUEST_PROCESSED_SUCCESSFULLY));
            responseDTO.setData(new FileInfo(filename, originalName, file.getSize()));
        }
        return responseDTO;
    }

    public InputStream getTaskAttachment(String filename) throws IOException {
        try {
            File file = new File(basePath + "task/" + filename);
            return FileUtils.openInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found", e);
        }
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
