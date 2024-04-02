package ru.practicum.valid;

import ru.practicum.events.dto.EventEntityDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateValidatorForPublished implements ConstraintValidator<CheckEventDateAfter2Hours, EventEntityDto> {
    @Override
    public void initialize(CheckEventDateAfter2Hours checkEventDateAfter2Hours) {
    }

    @Override
    public boolean isValid(EventEntityDto eventEntityDto, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime eventDate = LocalDateTime.parse(eventEntityDto.getEventDate(), dateTimeFormatter);
        return eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
