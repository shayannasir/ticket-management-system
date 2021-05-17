package tech.shayannasir.tms.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tech.shayannasir.tms.constants.MessageConstants;
import tech.shayannasir.tms.dto.ResetPasswordDTO;
import tech.shayannasir.tms.dto.ResponseDTO;
import tech.shayannasir.tms.entity.FileInfo;
import tech.shayannasir.tms.enums.FileUploadType;
import tech.shayannasir.tms.exception.ValidationException;
import tech.shayannasir.tms.service.MessageService;
import tech.shayannasir.tms.util.FileManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    private static final Detector DETECTOR = new DefaultDetector(
            MimeTypes.getDefaultMimeTypes());
    private final List<String> allowedImageTypes;
    private final List<String> allowedDocTypes;

    public FileUploadController() {
        allowedImageTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg");
        allowedDocTypes = Arrays.asList("application/pdf", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/vnd.ms-powerpoint",
                "application/vnd.oasis.opendocument.presentation",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/wps-office.pdf", "application/zip",
                "text/plain","application/vnd.oasis.opendocument.spreadsheet","application/vnd.oasis.opendocument.spreadsheet-template",
                "application/xml","application/x-tika-msoffice","application/x-tika-msoffice","application/zip",
                "application/vnd.oasis.opendocument.text","application/vnd.oasis.opendocument.text-template","application/xml",
                "application/x-tika-msoffice","application/zip","application/vnd.oasis.opendocument.presentation-template","application/x-tika-ooxml");
    }

    @Autowired
    private FileManager fileManager;
    @Autowired
    private MessageService messageService;

    @PostMapping("/upload/user/cover")
    public ResponseDTO uploadUserCover(@RequestParam("file") MultipartFile file) throws IOException, ValidationException {
        if (!isValidImage(file)) {
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
        }
        if (file.isEmpty())
            return new ResponseDTO(Boolean.FALSE, "File cannot be empty");
        return fileManager.saveUserCover(file);
    }

    @GetMapping("/get/user/cover")
    public byte[] fetchUserCover(@RequestParam("fileName") String fileName) throws IOException {
        InputStream inputStream = fileManager.getUserCover(fileName);
        byte[] response = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return response;
    }

    @PostMapping("/upload/article/cover")
    public ResponseDTO uploadArticleCover(@RequestParam("file") MultipartFile file) throws IOException, ValidationException {
        if (!isValidImage(file)) {
            return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
        }
        if (file.isEmpty())
            return new ResponseDTO(Boolean.FALSE, "File cannot be empty");
        return fileManager.saveArticleCover(file);
    }

    @GetMapping("/get/article/cover")
    public byte[] fetchArticleCover(@RequestParam("fileName") String fileName) throws IOException {
        InputStream inputStream = fileManager.getArticleCover(fileName);
        byte[] response = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return response;
    }

    @PostMapping("/upload/ticket/attachment")
    public ResponseDTO uploadTicketAttachment(@RequestParam("file") MultipartFile file, @RequestParam("type") FileUploadType type, @RequestParam("id") String id) throws IOException, ValidationException {
        try {
            Long ID = Long.parseLong(id.trim());
            switch (type) {
                case IMAGE:
                    if (!isValidImage(file)) {
                        return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
                    }
                    break;
                case DOCUMENT:
                    if (!isValidDoc(file)) {
                        return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
                    }
                    break;
            }
            if (file.isEmpty())
                return new ResponseDTO(Boolean.FALSE, "File cannot be empty");
            return fileManager.saveTicketAttachment(file, ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, "Invalid Ticket ID");
        }
    }

    @GetMapping("/get/ticket/attachment")
    public void fetchTicketAttachment(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = fileManager.getTicketAttachment(fileName);
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.getOutputStream().flush();
            inputStream.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @PostMapping("/upload/task/attachment")
    public ResponseDTO uploadTaskAttachment(@RequestParam("file") MultipartFile file, @RequestParam("type") FileUploadType type, @RequestParam("id") String id) throws IOException, ValidationException {
        try {
            Long ID = Long.parseLong(id.trim());
            switch (type) {
                case IMAGE:
                    if (!isValidImage(file)) {
                        return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
                    }
                    break;
                case DOCUMENT:
                    if (!isValidDoc(file)) {
                        return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
                    }
                    break;
            }
            if (file.isEmpty())
                return new ResponseDTO(Boolean.FALSE, "File cannot be empty");
            return fileManager.saveTaskAttachment(file, ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, "Invalid Task ID");
        }
    }

    @GetMapping("/get/task/attachment")
    public void fetchTaskAttachment(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = fileManager.getTaskAttachment(fileName);
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.getOutputStream().flush();
            inputStream.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @PostMapping("/upload/article/attachment")
    public ResponseDTO uploadArticleAttachment(@RequestParam("file") MultipartFile file, @RequestParam("type") FileUploadType type, @RequestParam("id") String id) throws IOException, ValidationException {
        try {
            Long ID = Long.parseLong(id.trim());
            switch (type) {
                case IMAGE:
                    if (!isValidImage(file)) {
                        return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
                    }
                    break;
                case DOCUMENT:
                    if (!isValidDoc(file)) {
                        return new ResponseDTO(Boolean.FALSE, messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED));
                    }
                    break;
            }
            if (file.isEmpty())
                return new ResponseDTO(Boolean.FALSE, "File cannot be empty");
            return fileManager.saveArticleAttachment(file, ID);
        } catch (NumberFormatException nfe) {
            return new ResponseDTO(Boolean.FALSE, "Invalid Article ID");
        }
    }

    @GetMapping("/get/article/attachment")
    public void fetchArticleAttachment(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = fileManager.getArticleAttachment(fileName);
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.getOutputStream().flush();
            inputStream.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static String detectMimeType(final MultipartFile file) throws IOException {
        TikaInputStream tikaIS = null;
        try {
            tikaIS = TikaInputStream.get(file.getInputStream());

            /*
             * You might not want to provide the file's name. If you provide an Excel
             * document with a .xls extension, it will get it correct right away; but
             * if you provide an Excel document with .doc extension, it will guess it
             * to be a Word document
             */
            final Metadata metadata = new Metadata();
            // metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());

            return DETECTOR.detect(tikaIS, metadata).toString();
        } finally {
            if (tikaIS != null) {
                tikaIS.close();
            }
        }
    }

    private String isValidImageAsset(MultipartFile file) throws IOException {
        String message;
        String mimeType = detectMimeType(file);
        if (allowedImageTypes.contains(mimeType)) {
            message = "true";
        } else {
            message = messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED);
        }
        return message;
    }

    private String isValidFile(MultipartFile file) throws IOException {
        String message;
        String mimeType = detectMimeType(file);
        if (allowedDocTypes.contains(mimeType)) {
            message = "true";
        } else {
            message = messageService.getMessage(MessageConstants.FILE_TYPE_NOT_SUPPORTED);
        }
        return message;
    }

    private boolean isValidImage(MultipartFile file) throws IOException, ValidationException {
        String validateImage = isValidImageAsset(file);
        return validateImage.equals("true");
    }

    private boolean isValidDoc(MultipartFile file) throws IOException, ValidationException {
        String validateDoc = isValidFile(file);
        if (!validateDoc.equals("true")) {
            throw new ValidationException(validateDoc);
        }
        return true;
    }

}
