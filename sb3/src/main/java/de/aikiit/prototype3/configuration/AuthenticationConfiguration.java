package de.aikiit.prototype3.configuration;

import de.aikiit.prototype3.login.LeaveEventsUponLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.List;


@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // to make PreAuthorize work
@EnableWebSecurity
public class AuthenticationConfiguration {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Qualifier("Sb3UserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private LeaveEventsUponLogoutSuccessHandler leaveEventsUponLogoutSuccessHandler;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private DataSource dataSource;

    private String[] permitAllUris() {
        return List.of("/log**", "/register*", "/static/**", "/img/**", "/css/**", "/js/**", "/**.png", "/**.ico").toArray(new String[]{});
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.apply(Sb3CustomDsl.create());

        http.authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                        .requestMatchers(permitAllUris()).permitAll()
                        .anyRequest().authenticated()
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/login")
                        // .defaultSuccessUrl("/")
                        .permitAll()

        ).userDetailsService(userDetailsService);

        //.tokenRepository(persistentTokenRepository());
        //.rememberMe().tokenValiditySeconds(20000)
        // only SSL: .useSecureCookie(true)

        http.logout().permitAll()
                .logoutSuccessUrl("/login?success") // does not work with custom handler
                .logoutSuccessHandler(leaveEventsUponLogoutSuccessHandler)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true).invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID");

        return http.build();
    }

    // TODO .rememberMe((remember) -> remember
    //				.rememberMeServices(rememberMeServices)
    //			);
    // https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    RememberMeServices rememberMeServices(@Autowired UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("myKey", userDetailsService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }

    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error");
    }
}
