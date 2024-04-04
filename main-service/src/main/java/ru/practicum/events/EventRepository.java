package ru.practicum.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id = ?1 ORDER BY e.eventDate DESC")
    Page<Event> getAllEventsByUserId(long userId, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.id IN ?1")
    List<Event> getEventsForCompilation(List<Long> events);

    @Query(value = "SELECT e FROM Event e WHERE e.eventDate BETWEEN ?1 AND ?2")
    Page<Event> getEventsForAdminWithoutParameters(LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "e.category.id IN ?1 " +
            "AND e.eventDate BETWEEN ?2 AND ?3")
    Page<Event> getEventsForAdminOnlyCategories(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE e.state IN ?1 " +
            "AND e.eventDate BETWEEN ?2 AND ?3")
    Page<Event> getEventsForAdminOnlyStates(List<State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE e.state IN ?2 " +
            "AND e.category.id IN ?1 " +
            "AND e.eventDate BETWEEN ?3 AND ?4")
    Page<Event> getEventsForAdminWithCategoriesAndStates(List<Long> categories, List<State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id IN ?1 " +
            "AND e.eventDate BETWEEN ?2 AND ?3")
    Page<Event> getEventsForAdminOnlyUsers(List<Long> users, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id IN ?1 " +
            "AND e.category.id IN ?2 " +
            "AND e.eventDate BETWEEN ?3 AND ?4")
    Page<Event> getEventsForAdminWithUsersAndCategories(List<Long> users, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.eventDate BETWEEN ?3 AND ?4")
    Page<Event> getEventsForAdminWithUsersAndStates(List<Long> users, List<State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.category.id IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    Page<Event> getEventsForAdminWithAllParameters(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "(:text = '' OR (UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) " +
            "AND (e.category.id IN :cat) " +
            "AND (e.eventDate BETWEEN :start AND :end) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsAvailableTrueCategoriesExist(@Param("text") String text,
                                                      @Param("paid") Boolean paid,
                                                      @Param("cat") List<Long> categories,
                                                      @Param("start") LocalDateTime rangeStart,
                                                      @Param("end") LocalDateTime rangeEnd,
                                                      Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "(:text LIKE '' OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) " +
            "AND (e.category.id IN :cat) " +
            "AND (e.eventDate BETWEEN :start AND :end)")
    Page<Event> getEventsAvailableFalseCategoriesExist(@Param("text") String text,
                                                       @Param("paid") Boolean paid,
                                                       @Param("cat") List<Long> categories,
                                                       @Param("start") LocalDateTime rangeStart,
                                                       @Param("end") LocalDateTime rangeEnd,
                                                       Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "(:text = '' OR (UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) " +
            "AND (e.eventDate BETWEEN :start AND :end) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsAvailableTrueCategoriesNotExist(@Param("text") String text,
                                                         @Param("paid") Boolean paid,
                                                         @Param("start") LocalDateTime rangeStart,
                                                         @Param("end") LocalDateTime rangeEnd,
                                                         Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "(:text LIKE '' OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:paid IS NULL OR e.paid IS :paid) " +
            "AND (e.eventDate BETWEEN :start AND :end)")
    Page<Event> getEventsAvailableFalseCategoriesNotExist(@Param("text") String text,
                                                          @Param("paid") Boolean paid,
                                                          @Param("start") LocalDateTime rangeStart,
                                                          @Param("end") LocalDateTime rangeEnd,
                                                          Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "e.location.id = ?1 " +
            "AND e.state = 'PUBLISHED'")
    Page<Event> getEventsByLocationId(long locId, Pageable page);

    @Query(value = "SELECT e FROM Event e WHERE " +
            "e.location.title = ?1 " +
            "AND e.state = 'PUBLISHED'")
    Page<Event> getEventsByLocationTitle(String title, Pageable page);
}


