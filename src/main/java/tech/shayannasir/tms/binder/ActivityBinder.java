package tech.shayannasir.tms.binder;

import org.springframework.stereotype.Component;
import tech.shayannasir.tms.dto.ActivityRequestDTO;
import tech.shayannasir.tms.entity.Activity;
import tech.shayannasir.tms.enums.ActivityStatus;

import java.util.Objects;

@Component
public class ActivityBinder {

    public Activity bindToDocument(ActivityRequestDTO source) {
        Activity target = new Activity();

        target.setBody(source.getBody());
        target.setSource(source.getSource());
        target.setSourceID(source.getSourceID());
        target.setType(source.getType());

        if (Objects.nonNull(source.getStatus()))
            target.setStatus(source.getStatus());
        else
            target.setStatus(ActivityStatus.PUBLISHED);

        return target;

    }

}
