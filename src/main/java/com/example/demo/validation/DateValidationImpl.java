package com.example.demo.validation;

import com.example.demo.dto.ChildDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateValidationImpl implements ConstraintValidator<DateValidation, ChildDto> {
    @Override
    public boolean isValid(ChildDto dto, ConstraintValidatorContext constraintValidatorContext) {
        return dto.birthYear() != null && (dto.birthYear().isBefore(LocalDate.now()) || dto.birthYear().isEqual(LocalDate.now()));
    }
}
