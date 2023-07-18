package de.aikiit.prototype.seeding;


import de.aikiit.prototype.tenant.TenantRepository;
import de.aikiit.prototype.user.RoleRepository;
import de.aikiit.prototype.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.transaction.Transactional;

@Slf4j
@Configuration
public class BootstrapDataCreator {
    public static final String ROLE_PREFIX = "ROLE_";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public BootstrapDataCreator(RoleRepository roleRepository, UserRepository userRepository, TenantRepository tenantRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    /*
     * As example data needs to be bootstrapped in the correct order we do this synchronously.
     */
    @Bean
    @Profile("!test")
    @Transactional
    public CommandLineRunner bootstrapExampleAndBaseData() {
        return (args) -> {
            new ProfileSeedDataCreator().bootstrapRoleData(roleRepository);
            new UserSeedDataCreator().bootstrapUserRelatedData(roleRepository, userRepository, tenantRepository);
            log.info("Finished bootstrap data loading.");
        };
    }
}
