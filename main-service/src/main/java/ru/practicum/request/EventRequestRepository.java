package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.dto.ListEventRequestIdDto;
import ru.practicum.request.model.EventRequest;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    @Query(value = "SELECT COUNT(r.id) FROM EventRequest r WHERE r.event.id = ?1 AND r.status = 'CONFIRMED'")
    long getCountConfirmedRequest(long eventId);

    @Query(value = "SELECT r FROM EventRequest r WHERE r.requester.id = ?1")
    List<EventRequest> getEventRequestsByRequesterId(long userId);

    @Query(value = "SELECT r FROM EventRequest r WHERE r.event.id = ?1 AND r.event.initiator.id = ?2")
    List<EventRequest> getEventRequestsByEventId(long eventId, long userId);

    @Query(value = "SELECT r FROM EventRequest r WHERE r.id IN ?1")
    List<EventRequest> getEventRequestsByEventsId(List<Long> ids);
}
