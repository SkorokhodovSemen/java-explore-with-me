package ru.practicum.valid;

import ru.practicum.events.dto.EventAdminDto;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventEntityDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateValidatorForAdmin implements ConstraintValidator<CheckEventDatePublishedAfter1Hours, EventAdminDto> {
    @Override
    public void initialize(CheckEventDatePublishedAfter1Hours checkEventDatePublishedAfter1Hours) {
    }

    @Override
    public boolean isValid(EventAdminDto eventDto, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime publishedOn = LocalDateTime.parse(eventDto.getPublishedOn(), dateTimeFormatter);
        return publishedOn.isAfter(LocalDateTime.now().plusHours(1));
    }
}
