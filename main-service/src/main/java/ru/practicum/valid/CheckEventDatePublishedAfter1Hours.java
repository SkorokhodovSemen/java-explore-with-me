package ru.practicum.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EventDateValidatorForAdmin.class)
public @interface CheckEventDatePublishedAfter1Hours {
    String message() default "Event Date must be before 1 hours min";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
