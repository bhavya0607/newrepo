import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;

    // Retrieve all audit logs
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }

    // Retrieve logs for a specific user
    @GetMapping("/user")
    public ResponseEntity<List<AuditLog>> getUserAuditLogs(@RequestParam String email) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEmail(email));
    }

    // Deactivate account and log the action
    @PostMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount(@RequestParam String email, HttpServletRequest request) {
        userService.deactivateAccount(email, request);
        auditLogService.logActivity(email, "ACCOUNT_DEACTIVATED", "User account was deactivated.", request);
        return ResponseEntity.ok("Account deactivated successfully.");
    }

    // Delete account and log the action
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam String email, HttpServletRequest request) {
        userService.deleteAccount(email, request);
        auditLogService.logActivity(email, "ACCOUNT_DELETED", "User account was deleted.", request);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
