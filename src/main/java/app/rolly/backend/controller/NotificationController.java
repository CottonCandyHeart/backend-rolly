package app.rolly.backend.controller;

import app.rolly.backend.dto.NotificationDto;
import app.rolly.backend.model.User;
import app.rolly.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/")
    public ResponseEntity<?> getNotifications(Authentication authentication){
        return new ResponseEntity<>(
                notificationService.getNotifications((User) authentication.getPrincipal()),
                HttpStatus.OK
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNotification(@RequestBody NotificationDto notificationDto, Authentication authentication){
        if (notificationService.addNotification(notificationDto, (User) authentication.getPrincipal())){
            return new ResponseEntity<>("Notification added", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed: adding notification", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id){
        if (notificationService.markAsRead(id)){
            return new ResponseEntity<>("Notification marked as read", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed: cannot mark notification as read", HttpStatus.BAD_REQUEST);
    }


}
