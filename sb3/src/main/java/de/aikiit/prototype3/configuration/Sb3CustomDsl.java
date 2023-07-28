package de.aikiit.prototype3.configuration;

import de.aikiit.prototype3.login.LoginTenantAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Slf4j
public class Sb3CustomDsl extends AbstractHttpConfigurer<Sb3CustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(authenticationFilter(authenticationManager), LoginTenantAuthenticationFilter.class);
    }

    public static Sb3CustomDsl create() {
        return new Sb3CustomDsl();
    }

    public LoginTenantAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        LoginTenantAuthenticationFilter filter = new LoginTenantAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationFailureHandler(failureHandler());
        return filter;
    }

    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error");
    }

}