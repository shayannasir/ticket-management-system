package tech.shayannasir.tms.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    String name;
    String originalFilename;
    long sizeInBytes;

}
