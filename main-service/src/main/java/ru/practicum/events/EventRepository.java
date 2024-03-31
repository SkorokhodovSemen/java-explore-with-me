package ru.practicum.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e FROM Event e WHERE e.initiator.id = ?1 ORDER BY e.eventDate DESC")
    Page<Event> getAllEventsByUserId(long userId, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.id IN ?1")
    List<Event> getEventsForCompilation(List<Long> events);

    @Query(value = "SELECT e FROM Event e WHERE (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND (e.eventDate BETWEEN ?3 AND ?4) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsWithAllPaidAndOnlyAvailable(String text,
                                                     List<Long> categories,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND (e.eventDate BETWEEN ?3 AND ?4) " +
            "AND (e.participantLimit = (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED'))")
    Page<Event> getEventsWithAllPaidAndNotAvailable(String text,
                                                    List<Long> categories,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid IS TRUE " +
            "AND (e.eventDate BETWEEN ?3 AND ?4) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsWithOnlyPaidAndOnlyAvailable(String text,
                                                      List<Long> categories,
                                                      LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd,
                                                      Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid IS TRUE " +
            "AND (e.eventDate BETWEEN ?3 AND ?4)")
    Page<Event> getEventsWithOnlyPaidAndNotAvailable(String text,
                                                     List<Long> categories,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid IS FALSE" +
            "AND (e.eventDate BETWEEN ?3 AND ?4) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsWithNotPaidAndOnlyAvailable(String text,
                                                     List<Long> categories,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid IS FALSE " +
            "AND (e.eventDate BETWEEN ?3 AND ?4)")
    Page<Event> getEventsWithNotPaidAndNotAvailable(String text,
                                                    List<Long> categories,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Pageable pageable);

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

    @Query(value = "SELECT e FROM Event e WHERE e.category.id IN ?1 " +
            "AND (e.eventDate BETWEEN ?2 AND ?3) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsWithAllPaidAndOnlyAvailableWithoutText(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.category.id IN ?1 " +
            "AND (e.eventDate BETWEEN ?2 AND ?3)")
    Page<Event> getEventsWithAllPaidAndNotAvailableWithoutText(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.category.id IN ?1 " +
            "AND e.paid IS TRUE " +
            "AND (e.eventDate BETWEEN ?2 AND ?3) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsWithOnlyPaidAndOnlyAvailableWithoutText(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.category.id IN ?1 " +
            "AND e.paid IS TRUE " +
            "AND (e.eventDate BETWEEN ?2 AND ?3)")
    Page<Event> getEventsWithOnlyPaidAndNotAvailableWithoutText(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.category.id IN ?1 " +
            "AND e.paid IS FALSE" +
            "AND (e.eventDate BETWEEN ?2 AND ?3) " +
            "AND (e.participantLimit > (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) " +
            "OR ((e.participantLimit - (SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) < 0)")
    Page<Event> getEventsWithNotPaidAndOnlyAvailableWithoutText(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e FROM Event e WHERE e.category.id IN ?1 " +
            "AND e.paid IS FALSE " +
            "AND (e.eventDate BETWEEN ?2 AND ?3)")
    Page<Event> getEventsWithNotPaidAndNotAvailableWithoutText(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}

