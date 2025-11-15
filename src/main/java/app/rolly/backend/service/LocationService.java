package app.rolly.backend.service;

import app.rolly.backend.dto.LocationDto;
import app.rolly.backend.model.Location;
import app.rolly.backend.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public boolean addLocation(LocationDto locationDto){
        if (locationRepository.findByName(locationDto.getName()).isPresent()){
            return false;
        }

        locationRepository.save(new Location(
                locationDto.getName(),
                locationDto.getCity(),
                locationDto.getCountry(),
                locationDto.getLatitude(),
                locationDto.getLongitude()
        ));

        return true;
    }

    public boolean removeLocation(String locationName){
        if (locationRepository.findByName(locationName).isEmpty()){
            return false;
        }

        locationRepository.removeLocationByName(locationName);
        return true;
    }

    public boolean modifyLocation(String name, LocationDto locationDto){

        Optional<Location> oldLocation;

        if ((oldLocation = locationRepository.findByName(name)).isEmpty()){
            return false;
        }

        oldLocation.get().setName(locationDto.getName());
        oldLocation.get().setCity(locationDto.getCity());
        oldLocation.get().setCountry(locationDto.getCountry());
        oldLocation.get().setLatitude(locationDto.getLatitude());
        oldLocation.get().setLongitude(locationDto.getLongitude());

        locationRepository.save(oldLocation.get());

        return true;
    }

    public LocationDto getLocation(String name){
        Optional<Location> location = locationRepository.findByName(name);

        if (location.isEmpty()) {
            return null;
        }

        return new LocationDto(location.get());
    }

}
