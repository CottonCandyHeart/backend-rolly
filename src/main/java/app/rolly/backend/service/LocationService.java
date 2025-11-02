package app.rolly.backend.service;

import app.rolly.backend.dto.LocationDto;
import app.rolly.backend.model.Location;
import app.rolly.backend.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public boolean addLocation(LocationDto locationDto){
        if (locationRepository.findByName(locationDto.getName()) != null){
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
        if (locationRepository.findByName(locationName) == null){
            return false;
        }

        locationRepository.removeLocationByName(locationName);
        return true;
    }

    public boolean modifyLocation(String name, LocationDto locationDto){

        Location oldLocation;

        if ((oldLocation = locationRepository.findByName(name)) == null){
            return false;
        }

        oldLocation.setName(locationDto.getName());
        oldLocation.setCity(locationDto.getCity());
        oldLocation.setCountry(locationDto.getCountry());
        oldLocation.setLatitude(locationDto.getLatitude());
        oldLocation.setLongitude(locationDto.getLongitude());

        locationRepository.save(oldLocation);

        return true;
    }

    public LocationDto getLocation(String name){
        Location location = locationRepository.findByName(name);

        if (location == null) {
            return null;
        }

        return new LocationDto(location);
    }

}
