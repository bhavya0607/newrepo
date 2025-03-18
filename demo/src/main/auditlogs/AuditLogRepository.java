package com.example.auditlogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface AuditlogRepository extends JpaRepository<AuditLog, Long> {
    Optional<AuditLog> findByEmail(String email);
}
