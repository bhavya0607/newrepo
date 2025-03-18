package com.example.auditlogs;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public void logActivity(String email, String activity, String details, HttpServletRequest request) {
        Optional<User> user = userRepository.findByEmail(email);
        Long userId = user.map(User::getId).orElse(null);
        String ipAddress = request.getRemoteAddr();

        AuditLog log = new AuditLog(userId, email, activity, details, ipAddress);
        auditLogRepository.save(log);
    }

    //  Account Deactivation
    public void deactivateAccount(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        userRepository.save(user);

        logActivity(email, "ACCOUNT_DEACTIVATED", "User deactivated their account", request);
    }

    //  Account Deletion
    public void deleteAccount(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        logActivity(email, "ACCOUNT_DELETED", "User deleted their account", request);
    }
}
