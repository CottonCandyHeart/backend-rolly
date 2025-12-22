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
                notificationService.getNotifications(authentication.getName()),
                HttpStatus.OK
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNotification(@RequestBody NotificationDto notificationDto, Authentication authentication){
        System.out.println(authentication.getName());
        if (notificationService.addNotification(notificationDto, authentication.getName())){
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

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeNotification(@RequestBody NotificationDto notificationDto, Authentication authentication){
        if (notificationService.removeNotification(notificationDto, authentication.getName())) {
            return new ResponseEntity<>("Notification removed", HttpStatus.OK);
        }

        return new ResponseEntity<>("Failed: removing notification", HttpStatus.BAD_REQUEST);
    }
}
