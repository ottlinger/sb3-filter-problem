package de.aikiit.prototype3.seeding;

import de.aikiit.prototype3.tenant.Role;
import de.aikiit.prototype3.tenant.Tenant;
import de.aikiit.prototype3.tenant.TenantRepository;
import de.aikiit.prototype3.user.RoleRepository;
import de.aikiit.prototype3.user.User;
import de.aikiit.prototype3.user.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static de.aikiit.prototype3.seeding.BootstrapDataCreator.ROLE_PREFIX;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Slf4j
public class UserSeedDataCreator {
    static final List<String> TENANTS = List.of("A", "B", "C");
    static final List<String> USERS = List.of("auser", "buser", "cuser");

    private static Optional<Role> get(String roleName, RoleRepository roleRepository) {
        return roleRepository.findByName(ROLE_PREFIX + roleName);
    }

    private static String encryptPassword(String input) {
        return "{bcrypt}" + new BCryptPasswordEncoder().encode(input);
    }

    private static void prepareTenantData(TenantRepository tenantRepository) {
        log.info("Bootstrapping TENANT data ....");

        for (String name : TENANTS) {
            if (tenantRepository.findByName(name).isPresent()) {
                log.info("Tenant {} already exists, will skip tenant seeding for it.", name);
                continue;
            }
            tenantRepository.save(new Tenant(name));
        }

        log.info("TENANT - Bootstrapping finished with {} tenants", tenantRepository.count());
    }

    public void bootstrapUserRelatedData(@NotNull RoleRepository roleRepository, @NotNull UserRepository userRepository, @NotNull TenantRepository tenantRepository) {
        log.info("Bootstrapping USER data ....");

        prepareTenantData(tenantRepository);

        for (String tenant : TENANTS) {
            Optional<Tenant> targetTenant = tenantRepository.findByName(tenant);
            if (targetTenant.isEmpty()) {
                log.error("Tenant {} not found - will shutdown immediately.", tenant);
                System.exit(-1);
            }

            Role user = null;
            Role admin = null;
            try {
                user = get("USER", roleRepository).get();
                admin = get("ADMIN", roleRepository).get();
            } catch (NoSuchElementException e) {
                log.error("One of the expected roles does not exist. Will stop the application now.", e);
                System.exit(-1);
            }

            log.info("Bootstrapping example users");
            for (String username : USERS) {
                log.info("User: {}", userRepository.save(User.builder().organisation(targetTenant.get()).fullName("Fullname " + username).userName(username).passwordHash(encryptPassword(username)).roles(Arrays.asList(user, admin)).build()));
                log.info("Tenant {} is running with bootstrapped user {}", tenant, username);
            }
        }
        log.info("USER - Bootstrapping finished");
    }

}