package app.rolly.backend.controller;

import app.rolly.backend.dto.LocationDto;
import app.rolly.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/get/{name}")
    public ResponseEntity<?> getLocation(@PathVariable String name){
        return new ResponseEntity<>(locationService.getLocation(name), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLocation(@RequestBody LocationDto locationDto){
        if (locationService.addLocation(locationDto)) {
            return new ResponseEntity<>("Location added successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot add location", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/remove/{name}")
    public ResponseEntity<?> removeLocation(@PathVariable String name){
        if (locationService.removeLocation(name)) {
            return new ResponseEntity<>("Location removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot remove location", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/modify/{name}")
    public ResponseEntity<?> modifyLocation(@PathVariable String name, @RequestBody LocationDto newLocation){
        if (locationService.modifyLocation(name, newLocation)) {
            return new ResponseEntity<>("Location modified", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot modify location", HttpStatus.BAD_REQUEST);
        }
    }
}
