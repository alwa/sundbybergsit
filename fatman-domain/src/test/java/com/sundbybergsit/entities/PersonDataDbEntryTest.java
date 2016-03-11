package com.sundbybergsit.entities;

import org.junit.gen5.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PersonDataDbEntryTest {

    @Test
    public void testValidation() {
        PersonDataDbEntry entry = new PersonDataDbEntry(new FatmanDbUser("tjoho", 180, new java.sql.Date(new Date().getTime()), "Tjo", "Ho"), 80f, 20f, 0f,
                new java.sql.Date(new Date().getTime()), 2);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PersonDataDbEntry>> errors = validator.validate(entry);

        assertThat(errors.size(), is(0));
    }

    @Test
    public void testValidationFailsNotEnoughWeight() {
        PersonDataDbEntry entry = new PersonDataDbEntry(new FatmanDbUser("tjoho", 180, new java.sql.Date(new Date().getTime()), "Tjo", "Ho"), 29f, 20f, 0f,
                new java.sql.Date(new Date().getTime()), 2);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PersonDataDbEntry>> errors = validator.validate(entry);

        assertThat(errors.size(), is(1));
    }

    @Test
    public void testValidationFailsTooMuchWeight() {
        PersonDataDbEntry entry = new PersonDataDbEntry(new FatmanDbUser("tjoho", 180, new java.sql.Date(new Date().getTime()), "Tjo", "Ho"), 601f, 20f, 0f,
                new java.sql.Date(new Date().getTime()), 2);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PersonDataDbEntry>> errors = validator.validate(entry);

        assertThat(errors.size(), is(1));
    }

    @Test
    public void testValidationFailsTooMuchFat() {
        PersonDataDbEntry entry = new PersonDataDbEntry(new FatmanDbUser("tjoho", 180, new java.sql.Date(new Date().getTime()), "Tjo", "Ho"), 80f, 101f, 0f,
                new java.sql.Date(new Date().getTime()), 2);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PersonDataDbEntry>> errors = validator.validate(entry);

        assertThat(errors.size(), is(1));
    }
}