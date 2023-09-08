package com.cycnet.ctfPlatform.validators;

import com.cycnet.ctfPlatform.customAnnotations.ValidEventRequest;
import com.cycnet.ctfPlatform.dto.event.EventRequestDto;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class EventRequestDtoValidator implements ConstraintValidator<ValidEventRequest, EventRequestDto> {

    @Override
    public boolean isValid(EventRequestDto eventRequestDto, ConstraintValidatorContext context) {
        if (eventRequestDto == null) {
            return true;
        }

        boolean isValid = true;

        ZonedDateTime startedAt = null;
        ZonedDateTime endedAt = null;

        if (StringUtils.isBlank(eventRequestDto.endedAt())) {
            handleInvalidDate(context, "EndedAt cannot be blank", "endedAt");
            isValid = false;
        }

        if (StringUtils.isBlank(eventRequestDto.startedAt())) {
            handleInvalidDate(context, "StartedAt cannot be blank", "startedAt");
            isValid = false;
        }

        if(isValid) {
            try {
                startedAt = ZonedDateTime.parse(eventRequestDto.startedAt());
            } catch (DateTimeParseException e) {
                handleInvalidDate(context, "Invalid date format, use ISO-8601", "startedAt");
                isValid = false;
            }

            try {
                endedAt = ZonedDateTime.parse(eventRequestDto.endedAt());
            } catch (DateTimeParseException e) {
                handleInvalidDate(context, "Invalid date format, use ISO-8601", "endedAt");
                isValid = false;
            }
        }


        if (isValid && !startedAt.getZone().equals(endedAt.getZone())) {
            handleInvalidDate(context, "StartedAt and endedAt must have the same time zone", "startedAt");
            handleInvalidDate(context, "StartedAt and endedAt must have the same time zone", "endedAt");
            isValid = false;
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Z"));

        if (isValid && (startedAt.isAfter(endedAt) || startedAt.isBefore(currentDateTime) || endedAt.isBefore(currentDateTime))) {
            if (startedAt.isAfter(endedAt)) {
                handleInvalidDate(context, "StartedAt must be before endedAt", "startedAt");
            }
            if (startedAt.isBefore(currentDateTime)) {
                handleInvalidDate(context, "StartedAt must be in the future", "startedAt");
            }
            if (endedAt.isBefore(currentDateTime)) {
                handleInvalidDate(context, "EndedAt must be in the future", "endedAt");
            }
            isValid = false;
        }

        return isValid;
    }

    private void handleInvalidDate(ConstraintValidatorContext context, String message, String propertyName) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }

}
