package de.aikiit.prototype.user;

import de.aikiit.prototype.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u, Tenant t WHERE lower(u.userName) = lower(:userName) AND u.organisation = t AND lower(t.name) = lower(:tenantName)")
    Optional<User> getForLogin(String userName, String tenantName);

    @Query("SELECT u FROM User u, Tenant t WHERE u.userName = :userName AND u.organisation = :tenant")
    Optional<User> getByUserInTenant(String userName, Tenant tenant);

    @Query(value = "SELECT count(*) FROM PERSISTENT_LOGINS", nativeQuery = true)
    Long countPersistentLogins();

    Long deleteAllByOrganisation(Tenant tenant);

    Optional<User> findByUserNameIgnoreCase(String userName);

    long countAllByOrganisation(Tenant tenant);
}