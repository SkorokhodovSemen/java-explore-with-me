package ru.practicum.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.location.model.LocationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public LocationDto createLocation(LocationDto locationDto) {
        List<Location> locationDtos = locationRepository.findNotUnknownLocation();
        if (locationDtos.size() != 0) {
            for (Location location : locationDtos) {
                List<Float> floats = locationRepository.findRangeToLocation(locationDto.getLat(), locationDto.getLon(),
                        location.getLat(), location.getLon());
                if (locationDto.getRad() > floats.get(0)) {
                    log.info("Location is exist");
                    throw new ValidationException("This Location is exist");
                }
            }
        }
        return LocationMapper.toLocationDto(locationRepository.save(LocationMapper.toLocation(locationDto)));
    }

    @Override
    @Transactional
    public LocationDto updateLocation(long locId, LocationDto locationDto) {
        Location location = locationRepository.findById(locId).orElseThrow(() -> {
            log.info("Location with id = {} not found", locId);
            return new NotFoundException("Location not found");
        });
        if (locationDto.getCity() != null) {
            location.setCity(locationDto.getCity());
        }
        if (locationDto.getRad() != null) {
            location.setRad(locationDto.getRad());
        }
        if (locationDto.getLon() != null) {
            location.setLon(locationDto.getLon());
        }
        if (locationDto.getLat() != null) {
            location.setLat(locationDto.getLat());
        }
        if (locationDto.getTitle() != null) {
            location.setTitle(locationDto.getTitle());
        }
        if (locationDto.getDescription() != null) {
            location.setCity(locationDto.getCity());
        }
        return LocationMapper.toLocationDto(locationRepository.save(location));
    }

    @Override
    @Transactional
    public void deleteLocationById(long locId) {
        locationRepository.findById(locId).orElseThrow(() -> {
            log.info("Location with id = {} not found", locId);
            return new NotFoundException("Location not found");
        });
        locationRepository.deleteById(locId);
    }

    @Override
    public List<LocationDto> getLocations(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        return locationRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDto getLocationsById(long locId) {
        Location location = locationRepository.findById(locId).orElseThrow(() -> {
            log.info("Location with id = {} not found", locId);
            return new NotFoundException("Location not found");
        });
        return LocationMapper.toLocationDto(location);
    }

    @Override
    @Transactional
    public List<LocationDto> updateUnknownLocations(List<LocationDto> locationDtos) {
        List<Long> ids = new ArrayList<>();
        for (LocationDto locationDto : locationDtos) {
            ids.add(locationDto.getId());
        }
        List<Location> locations = locationRepository.findLocationByIds(ids);
        if (locations.size() == 0) {
            log.info("Location with id = {} not found", ids);
            throw new NotFoundException("Locations not found");
        }
        List<Location> updateLocations = new ArrayList<>();
        for (LocationDto locationDto : locationDtos) {
            Location location = new Location();
            location.setDescription(locationDto.getDescription());
            location.setRad(locationDto.getRad());
            location.setTitle(locationDto.getTitle());
            location.setLat(locationDto.getLat());
            location.setLon(locationDto.getLon());
            location.setCity(locationDto.getCity());
            location.setId(locationDto.getId());
            updateLocations.add(location);
            locationRepository.save(location);
        }
        return updateLocations.stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }
}
