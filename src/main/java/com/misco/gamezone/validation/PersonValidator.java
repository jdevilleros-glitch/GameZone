/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.validation;


import com.misco.gamezone.exceptions.BusinessRuleException;
import com.misco.gamezone.exceptions.InvalidDataException;
import com.misco.gamezone.exceptions.ResourceNotFoundException;
import com.misco.gamezone.model.Person;
import java.util.List;


/**
 *
 * @author USUARIO
 * 
 * Validates person information and business rules.
 */
public final class PersonValidator {

    private PersonValidator() {
        // Utility class.
    }

    /**
     * Validates the information required to register a person.
     */
    public static void validatePersonData(
            String id,
            String name,
            String email,
            String phone) {

        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException(
                    "id", "Person ID cannot be empty."
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException(
                    "name", "Person name cannot be empty."
            );
        }

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException(
                    "email", "Email cannot be empty."
            );
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidDataException(
                    "email", "Email must contain '@' and '.'."
            );
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidDataException(
                    "phone", "Phone number cannot be empty."
            );
        }
    }

    /**
     * Validates that a person exists.
     */
    public static void validatePersonExists(
            Person person,
            String personId) {

        if (person == null) {
            throw new ResourceNotFoundException("Person", personId);
        }
    }

    /**
     * Validates that a person ID is not already registered.
     */
    public static void validateUniquePerson(
            List<Person> existing,
            String newId) {

        if (existing == null) {
            return;
        }

        for (Person person : existing) {
            if (person != null && person.getId().equals(newId)) {
                throw new BusinessRuleException(
                        "A person with ID " + newId + " already exists."
                );
            }
        }
    }
}
