package ru.practicum.location.model;

import ru.practicum.location.dto.LocationDto;

public abstract class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setRad(locationDto.getRad());
        location.setCity(locationDto.getCity());
        location.setDescription(locationDto.getDescription());
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        location.setTitle(locationDto.getTitle());
        return location;
    }

    public static LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setRad(location.getRad());
        locationDto.setCity(location.getCity());
        locationDto.setDescription(location.getDescription());
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        locationDto.setTitle(location.getTitle());
        return locationDto;
    }
}
