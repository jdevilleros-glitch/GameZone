/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.exceptions;

/**
 *
 * @author USUARIO
 *
 * Represents an error caused by invalid input data.
 */
public class InvalidDataException extends GameZoneException {

    /**
     * Creates an exception for an invalid field.
     *
     * @param fieldName name of the invalid field
     * @param description explanation of the validation error
     */
    public InvalidDataException(String fieldName, String description) {
        super(
                "Invalid data for " + fieldName + ": " + description,
                "INVALID_DATA"
        );
    }
}
