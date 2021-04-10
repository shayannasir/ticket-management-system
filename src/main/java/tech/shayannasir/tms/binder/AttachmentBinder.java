package tech.shayannasir.tms.binder;

import org.aspectj.weaver.bcel.AtAjAttributes;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.entity.Attachment;
import tech.shayannasir.tms.entity.FileInfo;
import tech.shayannasir.tms.repository.AttachmentRepository;

@Component
public class AttachmentBinder {

    public Attachment bindToDocument(FileInfo source) {
        Attachment target = new Attachment();

        target.setName(source.getName());
        target.setOriginalName(source.getOriginalFilename());
        target.setSizeInBytes(source.getSizeInBytes());

        return target;
    }

}
