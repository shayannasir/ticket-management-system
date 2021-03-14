package tech.shayannasir.tms.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public abstract class BaseEntityDTO<ID extends Serializable> implements Serializable {

    private Date createdDate;

    private UserDTO createdBy;

    private Date lastModifiedDate;

    private UserDTO lastModifiedBy;

    protected abstract ID getId();
}
