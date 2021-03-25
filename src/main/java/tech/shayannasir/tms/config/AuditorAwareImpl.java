package tech.shayannasir.tms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import tech.shayannasir.tms.service.Impl.UserServiceImpl;

import java.util.Optional;

@Component("auditAware")
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(userService.getCurrentLoggedInUser().getId());
    }
}
