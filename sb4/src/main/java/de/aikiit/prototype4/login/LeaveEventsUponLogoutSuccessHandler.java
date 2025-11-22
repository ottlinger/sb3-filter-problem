package de.aikiit.prototype4.login;

import de.aikiit.prototype4.user.ApplicationUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LeaveEventsUponLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null && authentication.getPrincipal() instanceof ApplicationUser) {
            ApplicationUser userDetails = (ApplicationUser) authentication.getPrincipal();
            log.info("Logged out for tenant {}", userDetails.getTenant());

        } else {
            log.info("Unable to perform proper logout due to wrong class of UserCredentials, this is okay during test runs.");
        }

        // redirect to loginSuccessUrl :)
        String URL = request.getContextPath() + "/login?success";
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(URL);

        super.onLogoutSuccess(request, response, authentication);
    }

}
