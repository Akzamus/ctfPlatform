package com.cycnet.ctfPlatform.customAnnotations;

import com.cycnet.ctfPlatform.validators.EventRequestDtoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventRequestDtoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventRequest {

    String message() default "Invalid event request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
