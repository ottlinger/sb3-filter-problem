package de.aikiit.prototype.configuration;

import de.aikiit.prototype.login.LeaveEventsUponLogoutSuccessHandler;
import de.aikiit.prototype.login.LoginTenantAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // to make PreAuthorize work
@EnableWebSecurity
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Qualifier("Sb2UserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private LeaveEventsUponLogoutSuccessHandler leaveEventsUponLogoutSuccessHandler;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authenticationFilter(), LoginTenantAuthenticationFilter.class);

        http.authorizeRequests().antMatchers("/static/**").permitAll();
        http.authorizeRequests().antMatchers("/img/**").permitAll();
        http.authorizeRequests().antMatchers("/css/**").permitAll();
        http.authorizeRequests().antMatchers("/js/**").permitAll();
        http.authorizeRequests().antMatchers("/**.png").permitAll();

        http.authorizeRequests((authorizeRequests) ->
                authorizeRequests
                        .antMatchers("/log**").permitAll()
                        .antMatchers("/register*").permitAll()
                        .anyRequest().authenticated()
        );

        http.csrf().disable();

        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/login").defaultSuccessUrl("/")
                        .permitAll()
        ).rememberMe().tokenValiditySeconds(20000).userDetailsService(userDetailsService).tokenRepository(persistentTokenRepository()); // only SSL: .useSecureCookie(true)

        http.logout().permitAll()
                //.logoutSuccessUrl("/login?success") does not work with custom handler
                .logoutSuccessHandler(leaveEventsUponLogoutSuccessHandler)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true).invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    public LoginTenantAuthenticationFilter authenticationFilter() throws Exception {
        LoginTenantAuthenticationFilter filter = new LoginTenantAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(failureHandler());
        return filter;
    }

    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error");
    }
}
