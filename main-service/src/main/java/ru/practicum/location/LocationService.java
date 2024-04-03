package ru.practicum.location;

import ru.practicum.location.dto.LocationDto;

import java.util.List;

public interface LocationService {
    LocationDto createLocation(LocationDto locationDto);

    LocationDto updateLocation(long locId, LocationDto locationDto);

    void deleteLocationById(long locId);

    List<LocationDto> getLocations(int from, int size);

    LocationDto getLocationsById(long locId);

    List<LocationDto> updateUnknownLocations(List<LocationDto> locationDtos);
}
