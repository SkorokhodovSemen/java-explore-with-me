package ru.practicum.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EventDateValidatorForPublished.class)
public @interface CheckEventDateAfter2Hours {
    String message() default "Event Date must be after 2 hours min";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
