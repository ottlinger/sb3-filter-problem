package de.aikiit.prototype3.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
// working in psql:
// @Query("SELECT u FROM User u, Tenant t WHERE lower(u.userName) = lower(:userName) AND u.organisation = t AND lower(t.name) = lower(:tenantName)")
    @Query("SELECT u FROM User u, Tenant t WHERE lower(u.userName) = lower(:userName) AND u.organisation = t AND lower(t.name) = lower(:tenantName)")
    Optional<User> getForLogin(String userName, String tenantName);
}