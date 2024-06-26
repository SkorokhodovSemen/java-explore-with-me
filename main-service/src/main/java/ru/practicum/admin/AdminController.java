package ru.practicum.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.CategoryService;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.compilations.CompilationService;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.ResponseCompilationDto;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.EventAdminDto;
import ru.practicum.events.dto.EventAdminPatchDto;
import ru.practicum.events.dto.EventAdminSearchDto;
import ru.practicum.events.model.State;
import ru.practicum.location.LocationService;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.UserService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.valid.Admin;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final LocationService locationService;


    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids", defaultValue = "") List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get user with Ids = {}", ids);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Create new user = {}", userDto);
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("userId") long userId) {
        log.info("Delete user with id = {}", userId);
        userService.deleteUserById(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Validated(Create.class) @RequestBody CategoryDto categoryDto) {
        log.info("Create new category = {}", categoryDto);
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@Validated(Update.class) @RequestBody CategoryDto categoryDto,
                                      @PathVariable("catId") long catId) {
        log.info("Update category with id = {}", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("catId") long catId) {
        log.info("Delete category with id = {}", catId);
        categoryService.deleteCategoryById(catId);
    }

    @GetMapping("/events")
    public List<EventAdminSearchDto> getEvents(@RequestParam(name = "users", defaultValue = "") List<Long> users,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestParam(name = "states", defaultValue = "") List<State> states,
                                               @RequestParam(name = "categories", defaultValue = "") List<Long> categories,
                                               @RequestParam(name = "rangeStart", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd", defaultValue = "2500-01-01 00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        log.info("Get events for admin for userId = {} \n" +
                "states = {} \n" +
                "categories = {} \n" +
                "rangeStart = {} \n" +
                "rangeEnd = {}", users, states, categories, rangeStart, rangeEnd);
        return eventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventAdminPatchDto updateEventById(@Validated(Admin.class) @RequestBody EventAdminDto eventAdminDto,
                                              @PathVariable("eventId") long eventId) {
        log.info("Update event with id = {} \n" +
                "eventAdminDto = {}", eventId, eventAdminDto);
        return eventService.updateEventByIdForAdmin(eventAdminDto, eventId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Validated(Create.class) @RequestBody ResponseCompilationDto responseCompilationDto) {
        log.info("Create new compilation = {}", responseCompilationDto);
        return compilationService.createCompilation(responseCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable("compId") long compId) {
        log.info("Delete compilation with id = {}", compId);
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilationById(@Validated(Update.class) @RequestBody ResponseCompilationDto responseCompilationDto,
                                                @PathVariable("compId") long compId) {
        log.info("Update compilation = {}", compId);
        return compilationService.updateCompilationById(responseCompilationDto, compId);
    }

    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto createLocation(@Validated(Create.class) @RequestBody LocationDto locationDto) {
        log.info("Create new location = {}", locationDto);
        return locationService.createLocation(locationDto);
    }

    @PatchMapping("/locations/{locId}")
    public LocationDto updateLocation(@Validated(Update.class) @RequestBody LocationDto locationDto,
                                      @PathVariable("locId") long locId) {
        log.info("Update location with id = {}", locId);
        return locationService.updateLocation(locId, locationDto);
    }

    @PatchMapping("/locations")
    public List<LocationDto> updateUnknownLocations(@Validated(Update.class) @RequestBody List<LocationDto> locationDtos) {
        log.info("Update location with body = {}", locationDtos);
        return locationService.updateUnknownLocations(locationDtos);
    }

    @DeleteMapping("/locations/{locId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocationById(@PathVariable("locId") long locId) {
        log.info("Delete location with id = {}", locId);
        locationService.deleteLocationById(locId);
    }

    @GetMapping("/locations")
    public List<LocationDto> getLocations(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get locations for admin");
        return locationService.getLocations(from, size);
    }

    @GetMapping("/locations/{locId}")
    public LocationDto getLocationsById(@PathVariable("locId") long locId) {
        log.info("Get locations for admin by id = {}", locId);
        return locationService.getLocationsById(locId);
    }
}
