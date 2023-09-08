package com.cycnet.ctfPlatform.validators;

import com.cycnet.ctfPlatform.customAnnotations.ValidEventRequest;
import com.cycnet.ctfPlatform.dto.event.EventRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZonedDateTime;
import java.util.Objects;

public class EventRequestDtoValidator implements ConstraintValidator<ValidEventRequest, EventRequestDto> {

    @Override
    public boolean isValid(EventRequestDto eventRequestDto, ConstraintValidatorContext context) {
        if (eventRequestDto == null) {
            return true;
        }

        ZonedDateTime startedAt = eventRequestDto.startedAt();
        ZonedDateTime endedAt = eventRequestDto.endedAt();

        if (Objects.isNull(startedAt) || Objects.isNull(endedAt)) {
           return false;
        }

        if (!startedAt.getZone().equals(endedAt.getZone())) {
            handleInvalidDate(context, "StartedAt and endedAt must have the same time zone", "startedAt");
            handleInvalidDate(context, "StartedAt and endedAt must have the same time zone", "endedAt");
            return false;
        }

        if (endedAt.isBefore(startedAt) || endedAt.isEqual(startedAt)) {
            handleInvalidDate(context, "StartedAt must be before endedAt", "startedAt");
            return false;
        }

        return true;
    }

    private void handleInvalidDate(ConstraintValidatorContext context, String message, String propertyName) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }

}
